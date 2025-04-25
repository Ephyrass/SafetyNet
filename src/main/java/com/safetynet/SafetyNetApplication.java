package com.safetynet;

import com.safetynet.service.DataLoaderService;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;


@SpringBootApplication
public class SafetyNetApplication {

    public static void main(String[] args) {

        DataLoaderService dataLoader = new DataLoaderService();

        dataLoader.loadData();
        SpringApplication.run(SafetyNetApplication.class, args);
        // print the number of persons, fire stations, and medical records
        System.out.println("Data loaded successfully from JSON file : " + dataLoader.getDataStore());
    }

}
