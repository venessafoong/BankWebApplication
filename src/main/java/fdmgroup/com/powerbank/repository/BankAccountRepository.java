package fdmgroup.com.powerbank.repository;

import fdmgroup.com.powerbank.model.User;
import fdmgroup.com.powerbank.model.BankAccount;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    Optional<BankAccount> findByUser(User user);

	/**
	 * This function updates the balance of a bank account.
	 * 
	 * @param accNumber
	 * @param balance
	 * @return The number of bank accounts updated
	 */
	@Transactional
	@Modifying
	@Query("UPDATE BankAccount b SET b.balance=:newBalance WHERE b.number=:accNumber")
	int updateAccountBalance(@Param("accNumber") long accNumber, @Param("newBalance") double balance);

}
