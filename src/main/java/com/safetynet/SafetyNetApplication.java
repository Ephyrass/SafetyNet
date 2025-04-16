package com.safetynet;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;


@SpringBootApplication
public class SafetyNetApplication {

    public static void main(String[] args) {

        SpringApplication.run(SafetyNetApplication.class, args);
        System.out.println("Hello, SafetyNet!");
    }

}
