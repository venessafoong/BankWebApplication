package fdmgroup.com.powerbank.controller;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import fdmgroup.com.powerbank.model.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fdmgroup.com.powerbank.repository.InstalmentRepository;
import fdmgroup.com.powerbank.repository.MerchantRepository;
import fdmgroup.com.powerbank.service.CreditCardService;
import fdmgroup.com.powerbank.service.InstalmentService;

/**
 * Handles all of the simulate requests
 *
 * @author Randy
 */
@Controller
public class SimulateController {

    @Autowired
    InstalmentService instalmentService;

    @Autowired
    InstalmentRepository instalmentRepository;

    @Autowired
    CreditCardService creditCardService;

    @Autowired
    MerchantRepository merchantRepository;

    /* Get request simulation. Brings user to simualate instalment page
     */
    @GetMapping("/simulate_instalment")
    public String goToSimulateInstalment() {

        return "simulate_instalment";
    }

    /* Post request simulation
     */
    @PostMapping("/simulate_instalment")
    public String simulateInstalment(@RequestParam String creditCardNumber, @RequestParam String merchantId, @RequestParam LocalDate startDate,
                                     @RequestParam String duration, @RequestParam String principalAmount, Model model) {
        Instalment instalment = new Instalment();
        CreditCard creditCard = creditCardService.findCreditCardByCreditCardNumber(Long.parseLong(creditCardNumber));
        Optional<Merchant> optionalMerchant = merchantRepository.findById(Long.parseLong(merchantId));
        Merchant merchant = optionalMerchant.orElseThrow();
        instalment.setCreditCard(creditCard);
        instalment.setDuration(Integer.parseInt(duration));
        instalment.setStartDate(startDate);
        instalment.setPrincipleAmount(Double.parseDouble(principalAmount));
        instalment.setMerchant(merchant);

        Instalment savedInstalment = instalmentRepository.save(instalment);
        long instalmentId = savedInstalment.getId();

        instalmentService.generateMonthlyBillings(instalmentId);

        model.addAttribute("message", "instalment simulation successful!");

        return "simulate_instalment";
    }

    @GetMapping("/simulate_transaction")
    public String goToSimulateTransaction() {

        return "simulate_transaction";
    }

    @PostMapping("/simulate_transaction")
    public String simulateTransaction(@RequestParam String creditCardNumber, @RequestParam String merchantId,
                                      @RequestParam String amount, @RequestParam String currency, Model model) {
        currency = currency.toUpperCase();

        // load exchange rates from fx_rates
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Double> results;
        try {
            ExchangeRate exchangeRate = objectMapper.readValue(new File("./src/main/resources/fx_rates.json"), ExchangeRate.class);
            results = exchangeRate.getResults();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        if (!results.containsKey(currency)) {
            model.addAttribute("message", "Wrong currency name!");
            return "simulate_transaction";
        }


        CreditCardTransaction cctxn = new CreditCardTransaction();
        CreditCard creditCard = creditCardService.findCreditCardByCreditCardNumber(Long.parseLong(creditCardNumber));
        Optional<Merchant> optionalMerchant = merchantRepository.findById(Long.parseLong(merchantId));
        Merchant merchant = optionalMerchant.orElseThrow();


        // convert the amount from "currency" to USD
        double convertedAmount = this.convertCurrency(Double.parseDouble(amount), currency, results);

        if (creditCardService.createTransaction(creditCard.getCreditCardNumber(), convertedAmount, merchant.getId())) {
            model.addAttribute("message", "transaction simulation successful!");
        } else {
            model.addAttribute("message", "transaction simulation failed! Please check parameters");
        }

        return "simulate_transaction";
    }

    /**
     * convert any currency specified to USD
     *
     * @param amount
     * @param currency
     * @return
     */
    private double convertCurrency(double amount, String currency, Map<String, Double> results) {
        //round to 0.00
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String convertedAmount = decimalFormat.format(amount / results.get(currency));
        return Double.parseDouble(convertedAmount);
    }


    /**
     * update the exchange rates every day and save as fx_rates.json file in the resources directory
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateExchangeRate() {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.fastforex.io/fetch-all?api_key=7b9ca29f07-b8d01856da-rxazkh")
                    .get()
                    .addHeader("accept", "application/json")
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed to get response: " + response);
            }

            ResponseBody responseBody = response.body();
            String body = responseBody.string();

            // Convert JSON string to Java object
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            ExchangeRate exchangeRate = objectMapper.readValue(body, ExchangeRate.class);

            // Write Java object to file
            objectMapper.writeValue(new File("./src/main/resources/fx_rates.json"), exchangeRate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
