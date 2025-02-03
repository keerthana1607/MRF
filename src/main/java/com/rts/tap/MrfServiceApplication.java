package com.rts.tap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MrfServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MrfServiceApplication.class, args);
	}
}