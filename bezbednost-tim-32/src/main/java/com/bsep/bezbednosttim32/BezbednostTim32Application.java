package com.bsep.bezbednosttim32;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@SpringBootApplication
public class
BezbednostTim32Application {

	public static void main(String[] args) {
		SpringApplication.run(BezbednostTim32Application.class, args);
	}

}
