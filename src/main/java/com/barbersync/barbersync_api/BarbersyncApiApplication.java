package com.barbersync.barbersync_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BarbersyncApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BarbersyncApiApplication.class, args);
	}

}
