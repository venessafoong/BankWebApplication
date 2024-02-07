package fdmgroup.com.powerbank.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fdmgroup.com.powerbank.model.BankAccount;
import fdmgroup.com.powerbank.model.User;
import fdmgroup.com.powerbank.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepo;

	/** This method exists solely for testing UserService methods with Mockito.
	 * 
	 * @param userRepo
	 */
	public void setUserRepo(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	/**
	 * Verifies the user credentials and returns true if user is found with
	 * credentials provided and false if otherwise
	 * 
	 * @param username : String
	 * @param password : String
	 * @return boolean
	 */
	public boolean verifyUserCredentials(String username, String password) {
		Optional<User> userOptional = userRepo.findByUsername(username);

		if (userOptional.isPresent() && userOptional.get().getPassword().equals(password)) {
			return true;
		} else {
			return false;
		}
	}

	public Optional<User> findByUsername(String username) {
		Optional<User> userOptional = userRepo.findByUsername(username);
		return userOptional;
	}

	public void registerNewUser(User user) {
		userRepo.save(user);

	}

	public Optional<User> findById(String userId) {
		Optional<User> userOptional = userRepo.findByUserId(userId);
		return userOptional;
	}

	public User findUserByUsername(String username) {
		Optional<User> userOptional = userRepo.findByUsername(username);
		return userOptional.orElseThrow();
	}

	public void saveUser(User user) {
		userRepo.save(user);
	}

	public User findUserByUserId(String userId) {
		Optional<User> userOptional = userRepo.findByUserId(userId);
		return userOptional.orElseThrow();
	}
}
