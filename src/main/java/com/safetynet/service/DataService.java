package com.safetynet.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.model.DataStore;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;

@Getter
@Setter
@Service
@Slf4j
public class DataService {
    private DataStore dataStore;
    private static final String JSON_FILE_PATH = "src/main/resources/data.json";

    /*
     * This method is called after the bean is created and initializes the data store
     * by loading data from a JSON file.
     */
    @PostConstruct
    public void loadData() {
        try {
            log.info("Data loaded successfully from JSON file.");
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream  = getClass().getClassLoader().getResourceAsStream(JSON_FILE_PATH);
            dataStore = mapper.readValue(inputStream, DataStore.class);
            log.info("Data loaded successfully from JSON file : {} Persons, {} FireStations, {} MedicalRecords",
                    dataStore.getPersons().size(),
                    dataStore.getFireStations().size(),
                    dataStore.getMedicalRecords().size());
        } catch (Exception e) {
            log.error("Error loading data from JSON file: {}", e.getMessage());
        }
    }

    public void saveDataToFile() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(JSON_FILE_PATH), dataStore);
            log.info("Data saved successfully to JSON file.");
        } catch (Exception e) {
            log.error("Error saving data to JSON file: {}", e.getMessage());
        }
    }

}
