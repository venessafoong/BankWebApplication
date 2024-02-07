package fdmgroup.com.powerbank.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

/**
 * The BankAccount class contains a unique long id and a double balance.
 *
 * @author Ryan Tan and Dayan Melisov
 *
 */

@Entity
public class BankAccount {

    @Id
    private long number;
    private double balance;

    @OneToOne(mappedBy = "bankAccount")
	private User user;

	
	@OneToMany(mappedBy = "bankAccount", fetch = FetchType.EAGER)
	private List<BankAccountTransaction> transactions;
	
    public BankAccount(){
        super();
    }

	public BankAccount(long bankAccountNumber, double balance) {
		this.number = bankAccountNumber;
		this.balance = balance;
	}

	/**
	 * @return user associated with this bank account
	 */
	public User getUser() {
		return user;
	}


	/**
	 * @param the user object associated with this bank account
	 */
	public void setUser(User user) {
		this.user = user;
	}
	/**
	 * @param the bank account number of this account
	 */
	public void setNumber(long number) {
		this.number = number;
	}
	
	/**
	 * @return the Bank Account Number of this bank account
	 */
	public long getNumber() {
		return number;
	}




	public List<BankAccountTransaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<BankAccountTransaction> transactions) {
		this.transactions = transactions;
	}

	/**
	 * @return the balance of this bank account
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * @param  balance of this bank account
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}
}
