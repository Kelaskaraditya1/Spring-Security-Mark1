package com.starkindustries.Spring_Security1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringSecurity1Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurity1Application.class, args);
	}

}
