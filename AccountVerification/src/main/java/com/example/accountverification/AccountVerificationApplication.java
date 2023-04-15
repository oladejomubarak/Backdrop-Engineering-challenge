package com.example.accountverification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class AccountVerificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountVerificationApplication.class, args);
	}

}
