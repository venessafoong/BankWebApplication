package fdmgroup.com.powerbank.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Merchant {

	@Id
	@SequenceGenerator(name = "MERCHANT_SEQ_GNTR", sequenceName = "MERCHANT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MERCHANT_SEQ_GNTR")
	private long id;
	private int merchantCategoryCode;
	@Column(unique = true)
	private String name;
	@OneToMany(mappedBy = "merchant", fetch = FetchType.EAGER)
	private List<CreditCardTransaction> creditCardTransactions;
	@OneToMany(mappedBy = "merchant", fetch = FetchType.EAGER)
	private List<Instalment> instalments;
	
	public Merchant() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getMerchantCategoryCode() {
		return merchantCategoryCode;
	}

	public void setMerchantCategoryCode(int merchantCategoryCode) {
		this.merchantCategoryCode = merchantCategoryCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CreditCardTransaction> getCreditCardTransaction() {
		return creditCardTransactions;
	}

	public void setCreditCardTransaction(List<CreditCardTransaction> creditCardTransactions) {
		this.creditCardTransactions = creditCardTransactions;
	}

	public List<Instalment> getInstalment() {
		return instalments;
	}

	public void setInstalment(List<Instalment> instalments) {
		this.instalments = instalments;
	}
	
}
