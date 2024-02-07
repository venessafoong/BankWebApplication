package fdmgroup.com.powerbank.repository;


import java.time.LocalDateTime;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fdmgroup.com.powerbank.model.CreditCard;
import fdmgroup.com.powerbank.model.User;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

    ArrayList<CreditCard> findAllCreditCardsByUser(User user);

}
