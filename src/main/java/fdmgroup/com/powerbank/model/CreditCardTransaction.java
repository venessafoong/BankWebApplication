package fdmgroup.com.powerbank.model;

import java.time.LocalDateTime;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;


@Entity
public class CreditCardTransaction implements Comparable<CreditCardTransaction>{
    @Id
    @SequenceGenerator(name = "CREDIT_TRANSACTION_SEQ_GNTR", sequenceName = "CT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CREDIT_TRANSACTION_SEQ_GNTR")
    private long creditCardTransactionId;
    private LocalDateTime dateTime;
    private double chargeAmount;
    private String type;
    
    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "credit_card_number")
    private CreditCard creditCard;

    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "merchant", referencedColumnName = "name")
    private Merchant merchant;
    
    public CreditCardTransaction() {
        super();
    }

    public CreditCardTransaction(LocalDateTime dateTime, double chargeAmount, String type, CreditCard creditCard) {
        this.dateTime = dateTime;
        this.chargeAmount = chargeAmount;
        this.type = type;
        this.creditCard = creditCard;
    }

    public CreditCardTransaction(LocalDateTime dateTime, double chargeAmount, String type, CreditCard creditCard, Merchant merchant) {
        this.dateTime = dateTime;
        this.chargeAmount = chargeAmount;
        this.type = type;
        this.creditCard = creditCard;
        this.merchant = merchant;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public double getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(double chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}
    

	@Override
	public int compareTo(CreditCardTransaction o) {
		// TODO Auto-generated method stub
		if (o.getDateTime().compareTo(dateTime) >=0) {
			return 1;
		}
		
		else {
			return -1;
		}
	}
    
}
