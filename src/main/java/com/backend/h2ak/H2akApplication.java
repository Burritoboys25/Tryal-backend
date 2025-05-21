package com.backend.h2ak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class H2akApplication {

	public static void main(String[] args) {
		SpringApplication.run(H2akApplication.class, args);
	}

}
