package fdmgroup.com.powerbank;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PowerBankApplication {

	public static void main(String[] args) {

		SpringApplication.run(PowerBankApplication.class, args);

	}

}
