package fdmgroup.com.powerbank.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fdmgroup.com.powerbank.repository.BankAccountRepository;
import fdmgroup.com.powerbank.model.BankAccount;


@ExtendWith(MockitoExtension.class)
class BankAccountServiceTest {

	@Mock
	BankAccountRepository mockBankAccountRepo;
	
	@Test
	public void findBalanceById_methodCallsFindBankAccountByIdMethodInBankAccountRepository() {
		//arrange
		BankAccountService bankAccountService = new BankAccountService();
		bankAccountService.setBankAccountRepo(mockBankAccountRepo);
		
		//act
		try {
		bankAccountService.findBalanceByBankAccountNumber(1);
		} catch (NoSuchElementException e) {
			
		}
		//verify
		verify(mockBankAccountRepo, times(1)).findById(1L);
		
	}

}
