package fdmgroup.com.powerbank.service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import fdmgroup.com.powerbank.model.BankAccountTransaction;
import fdmgroup.com.powerbank.model.CreditCard;
import fdmgroup.com.powerbank.model.CreditCardTransaction;
import fdmgroup.com.powerbank.model.Merchant;
import fdmgroup.com.powerbank.model.User;
import fdmgroup.com.powerbank.repository.BankAccountTransactionRepository;
import fdmgroup.com.powerbank.repository.CreditCardRepository;
import fdmgroup.com.powerbank.repository.CreditCardTransactionRepository;
import fdmgroup.com.powerbank.repository.MerchantRepository;
import fdmgroup.com.powerbank.repository.UserRepository;

@Service
public class CreditCardService {
	
	@Autowired
	private BankAccountTransactionRepository bankAccountTransactionRepository;
	
    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CreditCardTransactionRepository creditCardTransactionRepository;

    @Autowired
    private MerchantRepository merchantRepository;
    
    // Saves credit card into SQL database
    public void saveCreditCard(CreditCard creditCard) {
        creditCardRepository.save(creditCard);
    }


    public ArrayList<CreditCard> findAllCreditCardsByUser(User user) {
        ArrayList<CreditCard> creditCards = creditCardRepository.findAllCreditCardsByUser(user);
        return creditCards;
    }


    public CreditCard findCreditCardByCreditCardNumber(long creditCardNumber) {
        Optional<CreditCard> creditCardOptional = creditCardRepository.findById(creditCardNumber);
        return creditCardOptional.orElseThrow();
    }


    /**
     * auto-update the statement amount at the last moment of every month
     */
    @Scheduled(cron = "59 59 23 L * ?")
    public void updateStatementAmount() {

        List<CreditCard> allCreditCards = creditCardRepository.findAll();
        for (CreditCard creditCard : allCreditCards) {
            double currBillCycleCreditCardTxn = 0;
            for (CreditCardTransaction cc : this.findAllCreditCardTransactionsByCreditCardForCurrentBillingCycle(creditCard.getCreditCardNumber())) {
                currBillCycleCreditCardTxn += cc.getChargeAmount();
            }
            double updatedStatementAmount = creditCard.getStatementAmount() + currBillCycleCreditCardTxn;
            creditCard.setStatementAmount(updatedStatementAmount);
            creditCardRepository.save(creditCard);
        }


    }

    /** Overload version of updatePaymentAmount to run at adhoc instead of being scheduled.
     * to update statement for one creditcard for the previous month's credit card transaction
     */
    public void initialiseStatementAmount(long creditCardNumber) {

        Optional<CreditCard> optionalCreditCard = creditCardRepository.findById(creditCardNumber);
        CreditCard creditCard = optionalCreditCard.get();

        double previousBillCycleCreditCardTxn = 0;
        for (CreditCardTransaction cc : this.findAllCreditCardTransactionsByCreditCardForPreviousBillingCycle(creditCardNumber)) {
        	previousBillCycleCreditCardTxn += cc.getChargeAmount();
        	}

        creditCard.setStatementAmount(previousBillCycleCreditCardTxn);
        creditCardRepository.save(creditCard);
        
        }

    
    /** Updates the available credit for the credit card where
     * Available Credit = Credit limit - Balance
     * @param creditCardNumber
     */
    public void updateAvailableCredit(long creditCardNumber) {

        Optional<CreditCard> optionalCreditCard = creditCardRepository.findById(creditCardNumber);
        CreditCard creditCard = optionalCreditCard.get();
        
        creditCard.setAvailableCredit(creditCard.getCreditLimit() - creditCard.getCurrentBalance());
        creditCardRepository.save(creditCard);
        }
    
    /**
     * Returns all credit card transaction associated with a credit card in the current billing cycle
     * It does so by searching for all credit card transactions within the month the search is conducted
     *
     * @param creditCardNumber : (long) the credit card number
     * @return Array list of credit card transactions
     */
    public ArrayList<CreditCardTransaction> findAllCreditCardTransactionsByCreditCardForCurrentBillingCycle(long creditCardNumber) {
        LocalDate currDateTime = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate nextMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());
        LocalDateTime startDateCurrMonth = currDateTime.atStartOfDay();
        LocalDateTime endDateCurrMonth = nextMonth.atStartOfDay().minus(1, ChronoUnit.SECONDS);
        
        return creditCardTransactionRepository.findAllCreditCardTransactionsByCreditCardWithinDate(startDateCurrMonth, endDateCurrMonth, creditCardNumber);
    }

    /**
     * Returns all credit card transaction associated with a credit card in the current billing cycle
     * It does so by searching for all credit card transactions within the month the search is conducted
     *
     * @param creditCardNumber : (long) the credit card number
     * @return Array list of credit card transactions
     */
    public ArrayList<CreditCardTransaction> findAllCreditCardTransactionsByCreditCardForPreviousBillingCycle(long creditCardNumber) {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        LocalDateTime startDateLastMonth = lastMonth.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        LocalDateTime endDateLastMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay().minus(1, ChronoUnit.SECONDS);
        
        return creditCardTransactionRepository.findAllCreditCardTransactionsByCreditCardWithinDate(startDateLastMonth, endDateLastMonth, creditCardNumber);
    }
    
    /**
     * Returns all credit card transaction associated with a credit card in the current billing cycle and any future instalments
     * It does so by searching for all credit card transactions from the month the search is conducted
     *
     * @param creditCardNumber : (long) the credit card number
     * @return Array list of credit card transactions
     */
    public ArrayList<CreditCardTransaction> findAllCreditCardTransactionsByCreditCardToUpdateCurrentBalance(long creditCardNumber) {
        LocalDate currDateTime = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDateTime startDateCurrMonth = currDateTime.atStartOfDay();
        return creditCardTransactionRepository.findAllCreditCardTransactionsByCreditCardToUpdateCurrentBalance(startDateCurrMonth, creditCardNumber);
    }

    /**
     * This function updates the current balance given the credit card whose details the user would like to view
     *
     * @param creditCard : CreditCard
     */
    public void updateCurrentBalance(CreditCard creditCard) {
        double currentBalance = creditCard.getStatementAmount();
        for (CreditCardTransaction creditCardTransaction : findAllCreditCardTransactionsByCreditCardToUpdateCurrentBalance(creditCard.getCreditCardNumber())) {
            currentBalance += creditCardTransaction.getChargeAmount();
        }
        creditCard.setCurrentBalance(currentBalance);
        creditCardRepository.save(creditCard);
    }

    /**
     * Returns cashbacks associated with a credit card in the current billing cycle
     * It does so by searching for all credit card transactions within the month the search is conducted
     * restricting the search to cashbacks only
     *
     * @param creditCardNumber : (long) the credit card number
     * @return Array list of credit card transactions
     */
    public ArrayList<CreditCardTransaction> findAllCashBacksByCreditCardForCurrentBillingCycle(long creditCardNumber) {
        LocalDate currDateTime = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate nextMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());
        LocalDateTime startDateCurrMonth = currDateTime.atStartOfDay();
        LocalDateTime endDateCurrMonth = nextMonth.atStartOfDay().minus(1, ChronoUnit.SECONDS);

        return creditCardTransactionRepository.findAllCashBacksByCreditCardForCurrentBillingCycle(startDateCurrMonth, endDateCurrMonth, creditCardNumber);
    }


    /**
     * Returns the total amount of the cashback associated with a credit card in the current billing cycle
     * It does so by searching for all credit card transactions within the month the search is conducted
     * restricting the search to cashbacks only
     *
     * @param creditCardNumber : (long) the credit card number
     * @return The amount of cashback
     */
    public double returnCashBackAmountForCurrentBillingCycle(long creditCardNumber) {
        double cashback = 0;
        for (CreditCardTransaction cc : findAllCashBacksByCreditCardForCurrentBillingCycle(creditCardNumber)) {
            cashback += cc.getChargeAmount();
        }

        return Math.abs(cashback);
    }

    /**
     * Pays the credit card from the associated user's
     * bank account and informs whether the payment is
     * successful or not
     *
     * @param creditCardNumber
     * @param paymentAmount
     * @param user
     * @return
     */
    public boolean payCreditCard(long creditCardNumber, double paymentAmount, User user) {
    	Optional<CreditCard> creditCardOptional = creditCardRepository.findById(creditCardNumber);
    	CreditCard creditCard = creditCardOptional.get();
    	double userBalance = user.getBankAccount().getBalance();
    	
    	if (paymentAmount < creditCard.getMinimumAmount() ) {
    		return false;
    	}
    	
    	else {
    		if (paymentAmount <= userBalance) {
		    	CreditCardTransaction ccTransaction = new CreditCardTransaction(LocalDateTime.now(), paymentAmount * (-1),
                        "payment", creditCard);
		    	creditCardTransactionRepository.save(ccTransaction);
		    	
		    	BankAccountTransaction bankAccTxn = new BankAccountTransaction(LocalDateTime.now(), (-1)*paymentAmount,
                        "payment for cc " + creditCardNumber, user.getBankAccount());
		    	bankAccountTransactionRepository.save(bankAccTxn);
		    	user.getBankAccount().setBalance(userBalance - paymentAmount);
		    	userRepository.save(user);
		    	
		    	return true;
    		}
    		
    		else {
    			return false;
    		}
    	}
    }
    
    /**
     * Creates a transaction for simulation purposes not without checking the 
     * available credit if the transaction can be performed
     * Triggers the following after creation
     * - Update Current Balance
     * - Generates a corresponding cashback from the amount
     * - Assign Merchant 
     * - Gets FX if applicable
     */
    public boolean createTransaction(long creditCardNumber, double amount, long merchantId) {
    	Optional<CreditCard> creditCardOptional = creditCardRepository.findById(creditCardNumber);
    	CreditCard creditCard = creditCardOptional.get();
    	Optional<Merchant> optionalMerchant = merchantRepository.findById(merchantId);
    	Merchant merchant = optionalMerchant.orElseThrow();
//    	Due to initialisation, the credit card available balance may not be accurate and it should be refreshed first
    	updateAvailableCredit(creditCardNumber);
    	if (creditCard.getAvailableCredit() >= amount ) {
    		
    		CreditCardTransaction cctxn = new CreditCardTransaction(LocalDateTime.now(), amount,
                    "purchase", creditCard, merchant);
        	creditCardTransactionRepository.save(cctxn);
        	
    		createCashBack(creditCardNumber, amount);
    		updateAvailableCredit(creditCardNumber);
    		updateCurrentBalance(creditCard);
    		
    		return true;
    	}
    	
    	else {
    		return false;
    	}
    }
    
    /**
     * Generates a cash cash transaction from a transaction
     * @param creditCardNumber
     * @param amount
     */
    public void createCashBack(long creditCardNumber, double amount) {
    	Optional<CreditCard> creditCardOptional = creditCardRepository.findById(creditCardNumber);
    	CreditCard creditCard = creditCardOptional.get();
    	DecimalFormat decimalFormat = new DecimalFormat("#.00");
    	String cashBackAmount = decimalFormat.format(creditCard.getCashBackRate() * amount);
    	CreditCardTransaction cctxn = new CreditCardTransaction(LocalDateTime.now(), (Double.parseDouble(cashBackAmount) * (-1)),
                "cashback", creditCard);
    	creditCardTransactionRepository.save(cctxn);
    	
    }
    
    

}
