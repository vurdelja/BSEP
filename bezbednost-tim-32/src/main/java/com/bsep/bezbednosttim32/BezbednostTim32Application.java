package com.bsep.bezbednosttim32;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class BezbednostTim32Application {

	public static void main(String[] args) {
		SpringApplication.run(BezbednostTim32Application.class, args);
	}

}
