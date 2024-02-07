package fdmgroup.com.powerbank.controller;

import fdmgroup.com.powerbank.model.BankAccount;
import fdmgroup.com.powerbank.model.CreditCard;
import fdmgroup.com.powerbank.model.User;
import fdmgroup.com.powerbank.service.BankAccountService;
import fdmgroup.com.powerbank.service.CreditCardService;
import fdmgroup.com.powerbank.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.ArrayList;


@Controller
public class DashboardController {
    @Autowired
    CreditCardService creditCardService;
    @Autowired
    BankAccountService bankAccountService;
    @Autowired
    UserService userService;
    @GetMapping("/dashboard")
    public String goToPersonalCabinet(HttpSession session, Model model){

        User user = (User) session.getAttribute("user");

        if (user == null) {
        	return "redirect:/";
        }
        
        BankAccount account = bankAccountService.findBankAccountByUser(user);
        ArrayList<CreditCard> cards =  creditCardService.findAllCreditCardsByUser(user);

        model.addAttribute("account", account);
        model.addAttribute("creditcards",cards);

        return "dashboard";
    }

    @GetMapping("/bank_account")
    public String goToBankAccount(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        BankAccount account = bankAccountService.findBankAccountByUser(user);
        model.addAttribute("account", account);
        return "bank_account";
    }

}
