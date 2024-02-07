package fdmgroup.com.powerbank.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fdmgroup.com.powerbank.model.Instalment;
import fdmgroup.com.powerbank.model.User;
import fdmgroup.com.powerbank.service.InstalmentService;
import fdmgroup.com.powerbank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fdmgroup.com.powerbank.model.CreditCard;
import fdmgroup.com.powerbank.model.CreditCardTransaction;
import fdmgroup.com.powerbank.model.User;
import fdmgroup.com.powerbank.service.CreditCardService;
import fdmgroup.com.powerbank.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/credit_card")
public class CreditCardController {
    @Autowired
    CreditCardService creditCardService;
    @Autowired
    InstalmentService instalmentService;
    @Autowired
    UserService userService;

    @GetMapping("/{number}")
    public String goToCreditCard(@PathVariable("number") int creditIndex, HttpSession session, Model model){
    	String username = (String) session.getAttribute("currentUsername");
    	// Check if user is logged in
    	if (username == null) {

    		// Redirect to login page if not logged in
    		return "redirect:/";
    	} else {
            ArrayList<CreditCard> cards =  creditCardService.findAllCreditCardsByUser(userService.findUserByUsername(username));
    		CreditCard creditCard = cards.get(creditIndex);
    		creditCardService.initialiseStatementAmount(creditCard.getCreditCardNumber());
    		creditCardService.updateCurrentBalance(creditCard);
    		creditCardService.updateAvailableCredit(creditCard.getCreditCardNumber());
    		session.setAttribute("creditIndex", creditIndex);
    		session.setAttribute("currentCreditCardNumber", creditCard.getCreditCardNumber());
    		model.addAttribute("card", creditCard);
    		return "credit_card";
    	}
    }

    @GetMapping("/instalment_plans")
    public String goToInstalmentPlans(HttpSession session, Model model){
        long currentCreditCardNumber = (long) session.getAttribute("currentCreditCardNumber");
        CreditCard currCreditCard = creditCardService.findCreditCardByCreditCardNumber(currentCreditCardNumber);
        ArrayList<Instalment> instalments = instalmentService.getAllPlans(currCreditCard);
        model.addAttribute("currentCreditCardNumber",currentCreditCardNumber);
        if (instalments.isEmpty()) {
            model.addAttribute("emptyList", "This credit card is not tied to any instalment plans");
        }
        model.addAttribute("instalments", instalments);
        return "instalment_plans";
    }

    @GetMapping("/payment")
    public String goToCreditCardPayment(HttpSession session, Model model){
    	String username = (String) session.getAttribute("currentUsername");
    	User currUser = userService.findByUsername(username).get();
    	long currentCreditCardNumber = (long) session.getAttribute("currentCreditCardNumber");
    	CreditCard currCreditCard = (CreditCard) creditCardService.findCreditCardByCreditCardNumber(currentCreditCardNumber);
    	double cashback = creditCardService.returnCashBackAmountForCurrentBillingCycle(currentCreditCardNumber);

    	model.addAttribute("currentCreditCardNumber",currentCreditCardNumber);
    	model.addAttribute("statementAmount",currCreditCard.getStatementAmount());
    	model.addAttribute("dueDate",currCreditCard.getPaymentDueDate());
    	model.addAttribute("cashBack",cashback);
    	model.addAttribute("minimumAmount",currCreditCard.getMinimumAmount());
    	model.addAttribute("bankAccNumber",currUser.getBankAccount().getNumber());

    	model.addAttribute("currBillingCycleTransactions",creditCardService.findAllCreditCardTransactionsByCreditCardForCurrentBillingCycle(currentCreditCardNumber));

        return "payment";
    }

    @PostMapping("/payment")
    public String payCreditCard(@RequestParam String paymentAmount, HttpSession session, Model model){
    	String username = (String) session.getAttribute("currentUsername");
    	User currUser = userService.findByUsername(username).get();
    	double paymentAmountEntered = Double.parseDouble(paymentAmount);
    	long currentCreditCardNumber = (long) session.getAttribute("currentCreditCardNumber");
    	CreditCard currCreditCard = (CreditCard) creditCardService.findCreditCardByCreditCardNumber(currentCreditCardNumber);
    	double cashback = creditCardService.returnCashBackAmountForCurrentBillingCycle(currentCreditCardNumber);

    	if (creditCardService.payCreditCard(currentCreditCardNumber, paymentAmountEntered, currUser)) {

    		model.addAttribute("message","Payment Successful");
//    		The following code is to populate the page
        	model.addAttribute("currentCreditCardNumber",currentCreditCardNumber);
        	model.addAttribute("statementAmount",currCreditCard.getStatementAmount());
        	model.addAttribute("dueDate",currCreditCard.getPaymentDueDate());
        	model.addAttribute("cashBack",cashback);
        	model.addAttribute("minimumAmount",currCreditCard.getMinimumAmount());
        	model.addAttribute("bankAccNumber",currUser.getBankAccount().getNumber());

    		return "payment";
    	}
    	else {
    		model.addAttribute("message","Please enter an amount that is higher than the minimum amount or ensure account has sufficient balance");
//    		The following code is to populate the page
        	model.addAttribute("currentCreditCardNumber",currentCreditCardNumber);
        	model.addAttribute("statementAmount",currCreditCard.getStatementAmount());
        	model.addAttribute("dueDate",currCreditCard.getPaymentDueDate());
        	model.addAttribute("cashBack",cashback);
        	model.addAttribute("minimumAmount",currCreditCard.getMinimumAmount());
        	model.addAttribute("bankAccNumber",currUser.getBankAccount().getNumber());
    		return "payment";
    	}
    }
    
    @GetMapping("/transaction_history")
    public String goToCreditCardHistory(HttpSession session, Model model){
    	String username = (String) session.getAttribute("currentUsername");

    	// Check if user is logged in
    	if (username == null) {

    		// Redirect to login page if not logged in
    		return "redirect:/";
    	} else {
    		Integer creditIndex = (Integer) session.getAttribute("creditIndex");

    		if (creditIndex == null) {

    			// Redirect to dashboard if somehow user is logged in but no credit card was selected at dashboard
    			return "redirect:/dashboard";

    		} else {
    			ArrayList<CreditCard> cards = creditCardService.findAllCreditCardsByUser(userService.findUserByUsername(username));
    			CreditCard creditCard = cards.get(creditIndex);
    			List<CreditCardTransaction> ccTxn = creditCard.getTransactions();
    			Collections.sort(ccTxn);

    			model.addAttribute("creditIndex", creditIndex);
    			model.addAttribute("username", username);
    			model.addAttribute("transactions", ccTxn);
    			return "transaction_history";
    		}
    	}
    }
    
}
