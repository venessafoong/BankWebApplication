package fdmgroup.com.powerbank.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class CreditCard {

    @Id
    private long creditCardNumber;
    private double currentBalance;
    @Column(nullable = false,columnDefinition = "double default 0.0")
    private double statementAmount;
    private double minimumAmount;
    private double creditLimit;
    private double cashBackRate;
    @Column(nullable = false,columnDefinition = "double default 0.0")
    private double availableCredit;
    @ManyToOne()
    private User user;
    
    @OneToMany(mappedBy="creditCard", fetch = FetchType.EAGER)
    private List<CreditCardTransaction> transactions;

    private LocalDate expirationDate;
    private LocalDate paymentDueDate;

    public CreditCard(){
        super();
    }

    public CreditCard(long creditCardNumber, double currentBalance, double statementAmount, double minimumAmount,
                      double creditLimit, double cashBackRate, User user, LocalDate expirationDate, LocalDate paymentDueDate, double availableCredit) {
        this.creditCardNumber = creditCardNumber;
        this.currentBalance = currentBalance;
        this.statementAmount = statementAmount;
        this.minimumAmount = minimumAmount;
        this.creditLimit = creditLimit;
        this.cashBackRate = cashBackRate;
        this.user = user;
        this.expirationDate = expirationDate;
        this.paymentDueDate = paymentDueDate;
        this.availableCredit = availableCredit;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(long creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public double getStatementAmount() {
        return statementAmount;
    }

    public void setStatementAmount(double statementAmount) {
        this.statementAmount = statementAmount;
    }

    public double getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(double minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public double getCashBackRate() {
        return cashBackRate;
    }

    public void setCashBackRate(double cashBackRate) {
        this.cashBackRate = cashBackRate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public LocalDate getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(LocalDate paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

	public List<CreditCardTransaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<CreditCardTransaction> transactions) {
		this.transactions = transactions;
	}

	public double getAvailableCredit() {
		return availableCredit;
	}

	public void setAvailableCredit(double availableCredit) {
		this.availableCredit = availableCredit;
	}
	
}
