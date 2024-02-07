package fdmgroup.com.powerbank.controller;

import fdmgroup.com.powerbank.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import fdmgroup.com.powerbank.model.BankAccount;
import fdmgroup.com.powerbank.model.BankAccountTransaction;
import fdmgroup.com.powerbank.service.BankAccountService;
import fdmgroup.com.powerbank.service.UserService;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/bank_account")
public class BankAccountController {

	@Autowired
	BankAccountService bankAccountService;
	
	@Autowired
	UserService userService;
	
	// URL to be updated
	@GetMapping("/transaction_history")
	public String viewTransactionHistory(HttpSession session, Model model) {
		String username = (String) session.getAttribute("currentUsername");
		
		// If there is no username saved in the current session (no one is logged on) do this
		if (username == null) {
			
			// For now return to root page, can change later 
			return "redirect:/";
			
		} else {
			BankAccount bankAccount = userService.findByUsername(username).get().getBankAccount();
			model.addAttribute("transactions", bankAccount.getTransactions());
			model.addAttribute("username", username);
			return "bankAccountTransactionHistory";
		}
	}

    @GetMapping("/deposit_withdraw")
    public String goToDepositWithdrawPage(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "deposit_withdraw";
    }


    @PostMapping("/deposit_withdraw")
    public String depositWithdrawMoney(@RequestParam(value="accNumber") long accNumber, @RequestParam(value="amount") float amount, @RequestParam(value="type") String type, Model model, RedirectAttributes redirectAttributes) {
		Optional<BankAccount> bankAccountOptional = bankAccountService.getBankAccByAccNum(accNumber);
		if (bankAccountOptional.isPresent()) {
			BankAccount account = bankAccountOptional.get();
			switch (type) {
				case "deposit":
					bankAccountService.depositMoney(account, amount);
					redirectAttributes.addFlashAttribute("success", "Successfully deposited $" + amount + " into account " + accNumber);
					break;
				case "withdraw":
					if (bankAccountService.withdrawMoney(account, amount) == 0) {
						redirectAttributes.addFlashAttribute("error", "Not enough money in account");
						return "redirect:/bank_account/deposit_withdraw";
					}
					redirectAttributes.addFlashAttribute("success", "Successfully withdrawn $" + amount + " into account " + accNumber);
			}
		}
        return "redirect:/bank_account";
    }

    @GetMapping("/transfer")
	public String GoToTransferMoneyPage(Model model, HttpSession session) {
		String currentUsername = (String) session.getAttribute("currentUsername");
		BankAccount bankAccountFrom = bankAccountService.getBankAccByUsername(currentUsername).get();
		model.addAttribute("currentAccount", bankAccountFrom);
		return "transfer";
	}
	
	@PostMapping("/transfer")
	public String transferMoney(@RequestParam long accountNumber, @RequestParam double transferAmount, Model model, HttpSession session) {
		if(bankAccountService.getBankAccByAccNum(accountNumber).isEmpty()) {
			model.addAttribute("message", "Recipient Account does not exist");
			String currentUsername = (String) session.getAttribute("currentUsername");
			BankAccount bankAccountFrom = bankAccountService.getBankAccByUsername(currentUsername).get();
			model.addAttribute("currentAccount", bankAccountFrom);
			return "transfer";
		}
		String currentUsername = (String) session.getAttribute("currentUsername");
		BankAccount bankAccountFrom = bankAccountService.getBankAccByUsername(currentUsername).get();
		if(bankAccountFrom.getBalance() < transferAmount) {
			model.addAttribute("message", "Insufficient funds for transfer");
			model.addAttribute("currentAccount", bankAccountFrom);
			return "transfer";
		}
		BankAccount bankAccountTo = bankAccountService.getBankAccByAccNum(accountNumber).get();
		if(bankAccountTo.equals(bankAccountFrom)) {
			model.addAttribute("message", "Transferring to the same account");
			model.addAttribute("currentAccount", bankAccountFrom);
			return "transfer";
		}
		bankAccountService.updateAccBalAndTran(bankAccountFrom, bankAccountTo, transferAmount);
		model.addAttribute("message", "Transfer successful!");
		model.addAttribute("currentAccount", bankAccountFrom);
		return "transfer";
	}

}
