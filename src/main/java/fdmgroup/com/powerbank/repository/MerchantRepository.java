package fdmgroup.com.powerbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fdmgroup.com.powerbank.model.Merchant;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long>{

}
