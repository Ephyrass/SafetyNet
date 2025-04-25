package com.safetynet.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataStore {
    private List<Person> persons;
    @JsonProperty("medicalrecords")
    private List<MedicalRecord> medicalRecords;
    @JsonProperty("firestations")
    private List<FireStation> fireStations;
}
