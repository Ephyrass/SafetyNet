package com.safetynet;

import com.safetynet.service.DataService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SafetyNetApplication {

    public static void main(String[] args) {

        DataService dataLoader = new DataService();

        SpringApplication.run(SafetyNetApplication.class, args);
    };
}
