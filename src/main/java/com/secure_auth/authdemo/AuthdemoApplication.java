package com.secure_auth.authdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin
@EnableAsync
@EnableScheduling
public class AuthdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthdemoApplication.class, args);
	}

}
