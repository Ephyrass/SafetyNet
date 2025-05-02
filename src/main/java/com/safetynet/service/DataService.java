package com.safetynet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.model.DataStore;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;

@Getter
@Setter
@Service
@Slf4j
public class DataService {
    private DataStore dataStore;

    @Value("${data.file.path:src/main/resources/data.json}")
    private String jsonFilePath;

    /*
     * This method is called after the bean is created and initializes the data store
     * by loading data from a JSON file.
     */
    @PostConstruct
    public void loadData() {
        try {
            log.info("Loading data from JSON file...");
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data.json");

            if (inputStream == null) {
                log.warn("data.json not found in resources. Creating empty DataStore.");
                dataStore = new DataStore();
                return;
            }

            dataStore = mapper.readValue(inputStream, DataStore.class);
            log.info("Data loaded successfully: {} Persons, {} FireStations, {} MedicalRecords",
                    dataStore.getPersons().size(),
                    dataStore.getFireStations().size(),
                    dataStore.getMedicalRecords().size());
        } catch (Exception e) {
            log.error("Error loading data from JSON file: {}", e.getMessage(), e);
            // Initialisation d'un DataStore vide en cas d'erreur pour éviter les NullPointerException
            dataStore = new DataStore();
        }
    }

    public void saveDataToFile() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonFilePath), dataStore);
            log.info("Data saved successfully to JSON file: {}", jsonFilePath);
        } catch (Exception e) {
            log.error("Error saving data to JSON file: {}", e.getMessage(), e);
        }
    }
}