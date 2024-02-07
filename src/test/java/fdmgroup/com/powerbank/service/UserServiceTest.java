package fdmgroup.com.powerbank.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fdmgroup.com.powerbank.repository.UserRepository;
import fdmgroup.com.powerbank.model.User;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	
	@Mock
	UserRepository mockUserRepo;
	
	@Test
	public void verifyUserCredentials_methodCallsFindByUsernameMethodInUserRepositoryOnce() {
		//arrange
		UserService userService = new UserService();
		userService.setUserRepo(mockUserRepo);
		
		//act
		try {
		userService.verifyUserCredentials("hello", "hello");
		} catch (NoSuchElementException e) {
		}
		//verify
		verify(mockUserRepo, times(1)).findByUsername("hello");
	}
	
	@Test
	public void verifyUserCredentials_methodReturnsTrueWhenUsernameAndPasswordMatch() {
		User user = new User();
		Optional<User> userOption = Optional.ofNullable(user);
		user.setUsername("hello");
		user.setPassword("goodbye");
		UserService userService = new UserService();
		userService.setUserRepo(mockUserRepo);
		when(mockUserRepo.findByUsername("hello")).thenReturn(userOption);
		
		assertTrue(userService.verifyUserCredentials("hello", "goodbye"));
	}
	
	@Test
	public void verifyUserCredentials_methodReturnsFalseWhenUsernameAndPasswordDoNotMatch1() {
		User user = new User();
		Optional<User> userOption = Optional.ofNullable(user);
		user.setUsername("hello");
		user.setPassword("goodbye");
		UserService userService = new UserService();
		userService.setUserRepo(mockUserRepo);
		when(mockUserRepo.findByUsername("hello")).thenReturn(userOption);
		
		assertFalse(userService.verifyUserCredentials("hello", "goodbye1"));
	}
	
	/**
	 * This test ensures that the correct UserRepository method is called when the findAccountByUsername
	 * method is called in the UserService class.
	 */
	@Test
	public void findUserByUsername_methodCallsFindByUsernameMethodInUserRepository() {
		//arrange
		UserService userService = new UserService();
		userService.setUserRepo(mockUserRepo);
		
		//act
		try {
		userService.findUserByUsername("hello");
		} catch (NoSuchElementException e) {
		}
		
		//verify
		verify(mockUserRepo, times(1)).findByUsername("hello");
		
	}
}
