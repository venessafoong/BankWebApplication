package fdmgroup.com.powerbank.controller;


import fdmgroup.com.powerbank.model.BankAccount;
import fdmgroup.com.powerbank.model.CreditCard;
import fdmgroup.com.powerbank.service.BankAccountService;
import fdmgroup.com.powerbank.service.CreditCardService;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fdmgroup.com.powerbank.model.User;
import fdmgroup.com.powerbank.service.UserService;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private CreditCardService creditCardService;
	@Autowired
	private BankAccountService bankAccountService;
	
	/* Get request for root. Returns login page
	 */
	@GetMapping("/")
	public String goToLogin(Model model, HttpSession session) {

		return "login";
	}

	/*
	 * Post request for login. Attempts to verify user according to username,
	 * returns an error message and redirects user to login page if not found
	 * Returns userDashboard if found
	 * 
	 */
	@PostMapping("/login")

	public String loginUser(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {


		if (userService.verifyUserCredentials(username, password)) {
			session.setAttribute("currentUsername", username);
			
			User user = userService.findUserByUsername(username);
			session.setAttribute("user", user);
			
			return "redirect:/dashboard";
		}else {
			model.addAttribute("error","user not found or password does not match");
			return "login";	


		}
	}

	/*
	 * Get request for register page. Returns register page
	 * 
	 */
	@GetMapping("/register")
	public String goToRegisterUser(Model model, HttpSession session) {
		User user = new User();
		model.addAttribute("user", user);
		return "register";
	}


	@PostMapping("/register")
	public String processUserRegistration(Model model, User user, @RequestParam String retypePassword) {
		// check whether the user is our customer
		Optional<User> existingUserOptional = userService.findById(user.getUserId());

		if (existingUserOptional.isEmpty()) {
			model.addAttribute("errorMsg", "You are not our customer");
			return "register";
		}
		// check whether these is same username
		Optional<User> userNameOptional = userService.findByUsername(user.getUsername());
		if (userNameOptional.isPresent()) {
			model.addAttribute("errorMsg", "Username already exists, please try another one");
			return "register";
		}
		// check whether the password is correct
		if (!user.getPassword().equals(retypePassword)) {
			model.addAttribute("errorMsg", "Passwords do not matched");
			return "register";
		}

		User existingUser = existingUserOptional.get();
		existingUser.setUsername(user.getUsername());
		existingUser.setPassword(user.getPassword());
		userService.registerNewUser(existingUser);

		return "redirect:/";

	}
	
	@GetMapping("logout")
	public String logoutUser(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

}
