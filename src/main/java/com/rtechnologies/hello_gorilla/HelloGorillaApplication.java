package com.rtechnologies.hello_gorilla;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelloGorillaApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloGorillaApplication.class, args);
		System.out.println("Hello Gorilla Application Running...!");
	}

}
