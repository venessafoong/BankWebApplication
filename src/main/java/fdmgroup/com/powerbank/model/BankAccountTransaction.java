package fdmgroup.com.powerbank.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

import java.time.LocalDateTime;

@Entity
public class BankAccountTransaction {

    @Id
    @SequenceGenerator(name = "BANKACCTRANSACTION_SEQ_GNTR", sequenceName = "BANKACCTRANSACTION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BANKACCTRANSACTION_SEQ_GNTR")
    private long id;
    private LocalDateTime dateTime;
    private double transferAmount;
    private String type;
    private long counterPartyBankAccountNumber;

    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "bank_account_number")
	private BankAccount bankAccount;
    
    public BankAccountTransaction() {
    	super();
    }

    public BankAccountTransaction(LocalDateTime dateTime, double transferAmount, String type,
                                  long counterPartyBankAccountNumber, BankAccount bankAccount) {
        this.dateTime = dateTime;
        this.transferAmount = transferAmount;
        this.type = type;
        this.counterPartyBankAccountNumber = counterPartyBankAccountNumber;
        this.bankAccount = bankAccount;
    }
    public BankAccountTransaction(LocalDateTime dateTime, double transferAmount, String type,
                                  BankAccount bankAccount) {
        this.dateTime = dateTime;
        this.transferAmount = transferAmount;
        this.type = type;
        this.bankAccount = bankAccount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public double getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(double transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getCounterPartyBankAccountNumber() {
        return counterPartyBankAccountNumber;
    }

    public void setCounterPartyBankAccountNumber(long counterPartyBankAccountNumber) {
        this.counterPartyBankAccountNumber = counterPartyBankAccountNumber;
    }
    
    public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

}
