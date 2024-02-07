package fdmgroup.com.powerbank.repository;

import fdmgroup.com.powerbank.model.CreditCard;
import fdmgroup.com.powerbank.model.Instalment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface InstalmentRepository extends JpaRepository<Instalment, Long> {

    ArrayList<Instalment> findAllInstalmentsByCreditCard (CreditCard creditCard);

}
