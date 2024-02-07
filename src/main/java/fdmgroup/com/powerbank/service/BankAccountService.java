package fdmgroup.com.powerbank.service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import fdmgroup.com.powerbank.model.BankAccountTransaction;
import fdmgroup.com.powerbank.model.User;
import fdmgroup.com.powerbank.repository.BankAccountTransactionRepository;
import fdmgroup.com.powerbank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fdmgroup.com.powerbank.model.BankAccount;
import fdmgroup.com.powerbank.repository.BankAccountRepository;

/**
 * This service class handles features relating to Bank Accounts.
 *
 * @author Dayan Melisov, Ryan Tan, Sean Ong & Venessa Foong
 *
 */
@Service
public class BankAccountService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private BankAccountRepository bankAccountRepo;

	@Autowired
	private BankAccountTransactionRepository bankAccountTransactionRepository;

	/*
	 * This function exists solely to enable mockito testing
	 *
	 */
	public void setBankAccountRepo(BankAccountRepository bankAccountRepo) {

		this.bankAccountRepo = bankAccountRepo;
	}

	/** This function saves the changes in bank account details to the database.
	 *
	 * @param bankAccount : BankAccount
	 */
	public void saveBankAccount(BankAccount bankAccount){
		bankAccountRepo.save(bankAccount);
    }

	/** This function retrieves the bank account given the user. If
	 * no such account exists, a NoSuchElementException is thrown
	 *
	 * @return the bank account associated with the user input
	 *
	 * @throws NoSuchElementException
	 */
	public BankAccount findBankAccountByUser(User user) {
		Optional<BankAccount> bankAccountOptional = bankAccountRepo.findByUser(user);
		return bankAccountOptional.orElseThrow();
	}

	/** This function retrieves the balance of an account given an account id. If
	 * no such account exists, a NoSuchElementException is thrown
	 *
	 * @return the balance of the bank account associated with the bank account
	 *         number input
	 *
	 * @throws NoSuchElementException
	 */
	public double findBalanceByBankAccountNumber(long number) throws NoSuchElementException {
		Optional<BankAccount> optionalAccount = bankAccountRepo.findById(number);
		return optionalAccount.orElseThrow().getBalance();
	}

	/**
	 * This function updates the balance of the respective bank account and creates
	 * a new transaction.
	 */
	public void depositMoney(BankAccount account, double amount) {
		long accNumber = account.getNumber();
		double newBalance = account.getBalance() + amount;
		BankAccountTransaction transaction = new BankAccountTransaction(LocalDateTime.now(), amount,
				"deposit", account);
		bankAccountTransactionRepository.save(transaction);
		bankAccountRepo.updateAccountBalance(accNumber, newBalance);
	}

	/**
	 * This function updates the balance of the respective bank account if the
	 * current balance is more or equals to the amount argument, and creates a new
	 * transaction.
	 */
	public int withdrawMoney(BankAccount account, double amount) {
		long accNumber = account.getNumber();
		double accountBalance = account.getBalance();
		if (accountBalance >= amount) {
			double newBalance = accountBalance - amount;
			BankAccountTransaction transaction = new BankAccountTransaction(LocalDateTime.now(), amount * -1,
					"withdraw", account);
			transaction.setBankAccount(account);
			bankAccountTransactionRepository.save(transaction);
			return bankAccountRepo.updateAccountBalance(accNumber, newBalance);
		}
		return 0;
	}

	/** This function retrieves the bank account if it exists given an account number.
	 *
	 * @return the bank account associated with the bank account number input
	 */
	public Optional<BankAccount> getBankAccByAccNum(long accountNumber) {
		return bankAccountRepo.findById(accountNumber);
	}

	/** This function retrieves the bank account if it exists given a username of the account holder.
	 *
	 * @return the bank account associated with the username input
	 */
	public Optional<BankAccount> getBankAccByUsername(String username) {
		User user = userRepo.findByUsername(username).get();
		return bankAccountRepo.findByUser(user);
	}

	/** This function updates both the sender's and recipient's bank account balance
	 * and creates their respective transaction records when money is transferred between 2 accounts.
	 *
	 * @param accFrom : BankAccount
	 * @param accTo : BankAccount
	 * @param transferAmount : double
	 */
	public void updateAccBalAndTran(BankAccount accFrom, BankAccount accTo, double transferAmount) {
		accFrom.setBalance(accFrom.getBalance() - transferAmount);
		bankAccountRepo.save(accFrom);
		accTo.setBalance(accTo.getBalance() + transferAmount);
		bankAccountRepo.save(accTo);
		BankAccountTransaction tran1 = new BankAccountTransaction(LocalDateTime.now(), transferAmount * -1,
				"transfer", accTo.getNumber(), accFrom);
		bankAccountTransactionRepository.save(tran1);
		BankAccountTransaction tran2 = new BankAccountTransaction(LocalDateTime.now(), transferAmount,
				"transfer", accFrom.getNumber(), accTo);
		bankAccountTransactionRepository.save(tran2);
	}

}
