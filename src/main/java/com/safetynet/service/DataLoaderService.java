package com.safetynet.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.model.DataStore;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Getter
@Service
@Slf4j
public class DataLoaderService {
    private DataStore dataStore;

    /*
     * This method is called after the bean is created and initializes the data store
     * by loading data from a JSON file.
     */
    @PostConstruct
    public void loadData() {
        try {
            log.info("Data loaded successfully from JSON file.");
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream  = getClass().getClassLoader().getResourceAsStream("data.json");
            dataStore = mapper.readValue(inputStream, DataStore.class);
            log.info("Data loaded successfully from JSON file : {} Persons, {} FireStations, {} MedicalRecords",
                    dataStore.getPersons().size(),
                    dataStore.getFireStations().size(),
                    dataStore.getMedicalRecords().size());
        } catch (Exception e) {
            log.error("Error loading data from JSON file: {}", e.getMessage());
        }
    }

}
