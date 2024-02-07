package fdmgroup.com.powerbank.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fdmgroup.com.powerbank.model.CreditCard;
import fdmgroup.com.powerbank.model.CreditCardTransaction;
import fdmgroup.com.powerbank.model.Instalment;
import fdmgroup.com.powerbank.repository.CreditCardTransactionRepository;
import fdmgroup.com.powerbank.repository.InstalmentRepository;

@Service
public class InstalmentService {

    @Autowired
    private InstalmentRepository instalmentRepo;

    @Autowired
    private CreditCardService creditCardService;
    
    @Autowired
    private CreditCardTransactionRepository creditCardTransactionRepository;
    
    public ArrayList<Instalment> getAllPlans(CreditCard creditCard) {
    	ArrayList<Instalment> unexpiredInstalments = new ArrayList<Instalment>();
    	for(Instalment instalment: instalmentRepo.findAllInstalmentsByCreditCard(creditCard)) {
        	if(instalment.getMonthsLeft() > 0) {
        		unexpiredInstalments.add(instalment);
        	}
        }
    	return unexpiredInstalments;
    }    
    
    /**
     * Generates all of the credit card transactions needed for the installment
     * plan from start date using duration and monthly amount to the creditcard
     */
    public void generateMonthlyBillings(long instalmentId) {
    	Optional<Instalment> optionalInstalment = instalmentRepo.findById(instalmentId);
    	Instalment instalment = optionalInstalment.orElseThrow();
    	for (int numberOfMonths =0 ; numberOfMonths < instalment.getDuration(); numberOfMonths++ ) {
    		CreditCardTransaction cctxn = new CreditCardTransaction();
    		cctxn.setChargeAmount(instalment.getAmountPerMonth());
    		cctxn.setCreditCard(instalment.getCreditCard());
    		cctxn.setMerchant(instalment.getMerchant());
    		cctxn.setType("instalment");
    		cctxn.setDateTime(instalment.getStartDate().atStartOfDay().plusMonths(numberOfMonths));
    		creditCardTransactionRepository.save(cctxn);
    	}
    }
    
}
