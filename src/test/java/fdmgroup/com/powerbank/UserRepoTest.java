package fdmgroup.com.powerbank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fdmgroup.com.powerbank.model.User;
import fdmgroup.com.powerbank.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserRepoTest {

	
	@Mock
	UserRepository userRepository;
	
	@Test
	public void FindByUserNameWithCorrectUserName() {
		
		User user = new User();
		
		String userId = "S1234567A";
		String username = "John";
		String password = "asd";
		String email = "asd@asd.com";
		String phoneNumber = "123456789";
		LocalDate birthDate = LocalDate.parse("1990-12-31");
		
		user.setBirthDate(birthDate);
		user.setEmail(email);
		user.setUsername(username);
		user.setUserId(userId);
		user.setPhoneNumber(phoneNumber);
		user.setPassword(password);
		
		Optional<User> optionalUser = Optional.of(user);
		
		when(userRepository.findByUsername(username)).thenReturn(optionalUser);

		assertEquals(userRepository.findByUsername(username), optionalUser);
		
	}

	@Test
	public void FindByUserNameWithWrongUserName() {
		
		User user = new User();
		
		String userId = "S1234567A";
		String username = "John";
		String password = "asd";
		String email = "asd@asd.com";
		String phoneNumber = "123456789";
		LocalDate birthDate = LocalDate.parse("1990-12-31");
		
		user.setBirthDate(birthDate);
		user.setEmail(email);
		user.setUsername(username);
		user.setUserId(userId);
		user.setPhoneNumber(phoneNumber);
		user.setPassword(password);
		
		Optional<User> optionalUser = Optional.of(user);
		
		assertNotEquals(userRepository.findByUsername("WrongUserName"), optionalUser);
		
	}
}
