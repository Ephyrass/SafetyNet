package com.safetynet;

import com.safetynet.service.DataLoaderService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SafetyNetApplication {

    public static void main(String[] args) {

        DataLoaderService dataLoader = new DataLoaderService();

        SpringApplication.run(SafetyNetApplication.class, args);
    };
}
