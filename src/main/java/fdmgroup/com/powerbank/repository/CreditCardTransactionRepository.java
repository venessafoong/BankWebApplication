package fdmgroup.com.powerbank.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fdmgroup.com.powerbank.model.CreditCardTransaction;

@Repository
public interface CreditCardTransactionRepository extends JpaRepository<CreditCardTransaction, Long>{

    /**
     * Returns all credit card transaction associated with a credit card in the current billing cycle
     * It does so by searching for all credit card transactions within the month the search is conducted
     * @param startDate Start date of the current month at 00:00:00
     * @param endDate End date of the current month at 23:59:59
     * @param creditCardNumber 
     * @return a list of credit card transactions
     */
    @Query(value = "from CreditCardTransaction c where c.dateTime BETWEEN :startDate AND :endDate AND c.creditCard.creditCardNumber = :creditCardNumber")
    ArrayList<CreditCardTransaction> findAllCreditCardTransactionsByCreditCardWithinDate(@Param("startDate")LocalDateTime startDate,
    		@Param("endDate")LocalDateTime endDate,
    		long creditCardNumber);
    
    /**
     * Returns cashbacks associated with a credit card in the current billing cycle
     * It does so by searching for all credit card transactions within the month the search is conducted
     * restricting the search to cashbacks only
     * @param startDate Start date of the current month at 00:00:00
     * @param endDate End date of the current month at 23:59:59
     * @param creditCardNumber 
     * @return a list of credit card transactions
     */
    @Query(value = "from CreditCardTransaction c where c.dateTime BETWEEN :startDate AND :endDate AND c.creditCard.creditCardNumber = :creditCardNumber AND c.type LIKE 'cashback'")
    ArrayList<CreditCardTransaction> findAllCashBacksByCreditCardForCurrentBillingCycle(@Param("startDate")LocalDateTime startDate,
    		@Param("endDate")LocalDateTime endDate,
    		long creditCardNumber);

    /**
     * Returns all credit card transaction associated with a credit card in the current billing cycle and all future instalments
     * It does so by searching for all credit card transactions from the month the search is conducted
     * @param startDate Start date of the current month at 00:00:00
     * @param creditCardNumber
     * @return a list of credit card transactions
     */
    @Query(value = "from CreditCardTransaction c where c.dateTime >= :startDate AND c.creditCard.creditCardNumber = :creditCardNumber")
    ArrayList<CreditCardTransaction> findAllCreditCardTransactionsByCreditCardToUpdateCurrentBalance(@Param("startDate")LocalDateTime startDate, long creditCardNumber);
    
}
