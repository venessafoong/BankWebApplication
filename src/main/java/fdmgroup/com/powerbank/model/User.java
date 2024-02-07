package fdmgroup.com.powerbank.model;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
@Component
public class User {

	@Id
	private String userId;
	
	@Column(unique=true)
	private String username;

	private String password;
	private String email;
	private String phoneNumber;
	private LocalDate birthDate;

	@OneToOne
	@JoinColumn(name = "acc_number")
	private BankAccount bankAccount;

	@OneToMany(mappedBy = "user")
	private List<CreditCard> cards;
	
	public User() {
		super();
		this.cards = new ArrayList<>();
	}


	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}



	public List<CreditCard> getCards() {
		return cards;
	}


	public void setCards(List<CreditCard> cards) {
		this.cards = cards;
	}


	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the birthDate
	 */
	public LocalDate getBirthDate() {

		return birthDate;
	}

	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return The user's bank account
	 */
	public BankAccount getBankAccount() {
		return bankAccount;
	}
	/**
	 * @param The user's bank account
	 */
	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}
	

	@Override
	public String toString() {
		return "User [userId=" + userId + ", username=" + username + ", password=" + password + ", email=" + email
				+ ", phoneNumber=" + phoneNumber + ", birthDate=" + birthDate + "]";
	}

}
