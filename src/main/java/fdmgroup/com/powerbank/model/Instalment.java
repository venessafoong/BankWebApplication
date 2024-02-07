package fdmgroup.com.powerbank.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;

@Entity
public class Instalment {

    @Id
    @SequenceGenerator(name = "INSTALMENT_SEQ_GNTR", sequenceName = "INSTALMENT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INSTALMENT_SEQ_GNTR")
    private long id;
    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "merchant", referencedColumnName = "name")
    private Merchant merchant;
    private double principleAmount;
    private int duration;
    @Transient
    private double amountPerMonth;
    @Transient
    private int monthsLeft;
    private LocalDate startDate;
    @ManyToOne
    @JoinColumn (name = "credit_card_number")
    private CreditCard creditCard;

    public Instalment() {
    }

    public Instalment(Merchant merchant, double principleAmount, int duration, LocalDate startDate, CreditCard creditCard) {
        this.merchant = merchant;
        this.principleAmount = principleAmount;
        this.duration = duration;
        this.startDate = startDate;
        this.creditCard = creditCard;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public double getPrincipleAmount() {
        return principleAmount;
    }

    public void setPrincipleAmount(double principleAmount) {
        this.principleAmount = principleAmount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getAmountPerMonth() {
        amountPerMonth = principleAmount / duration;
        return amountPerMonth;
    }

    public void setAmountPerMonth(double amountPerMonth) {
        this.amountPerMonth = amountPerMonth;
    }

    public int getMonthsLeft() {
        Period monthsPast = (Period.between(startDate, LocalDate.now()));
        monthsLeft = duration - (monthsPast.getYears() * 12 + monthsPast.getMonths());
        return monthsLeft;
    }

    public void setMonthsLeft(int monthsLeft) {
        this.monthsLeft = monthsLeft;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }
}
