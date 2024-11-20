package com.prashant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@ComponentScan("controller")  - would  be required if controller package is outside the default main package

//@EnableWebSecurity  // optional in case of spring boot environment. 

//@EntityScan("com.prashant.model")
//@EnableJpaRepositories("com.prashant.repository")   // optional as we created the packages under main package only
@SpringBootApplication
public class EazyBankBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EazyBankBackendApplication.class, args);
	}

}
