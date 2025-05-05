package com.safetynet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.model.MedicalRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddMedicalRecord() throws Exception {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("Test");
        medicalRecord.setLastName("User");
        medicalRecord.setBirthdate("01/01/1990");
        medicalRecord.setMedications(Arrays.asList("med1:100mg", "med2:200mg"));
        medicalRecord.setAllergies(Collections.singletonList("allergie1"));

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.birthdate").value("01/01/1990"));
    }

    @Test
    public void testUpdateMedicalRecord() throws Exception {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("Update");
        medicalRecord.setLastName("Test");
        medicalRecord.setBirthdate("01/01/1980");
        medicalRecord.setMedications(List.of("med1:100mg"));
        medicalRecord.setAllergies(Collections.singletonList("allergie1"));

        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medicalRecord)));

        medicalRecord.setMedications(List.of("med2:200mg"));
        medicalRecord.setAllergies(Arrays.asList("allergie2", "allergie3"));

        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.medications[0]").value("med2:200mg"))
                .andExpect(jsonPath("$.allergies[0]").value("allergie2"));
    }

    @Test
    public void testDeleteMedicalRecord() throws Exception {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("Delete");
        medicalRecord.setLastName("Test");
        medicalRecord.setBirthdate("01/01/1975");
        medicalRecord.setMedications(Collections.emptyList());
        medicalRecord.setAllergies(Collections.emptyList());

        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(medicalRecord)));

        mockMvc.perform(delete("/medicalRecord")
                        .param("firstName", "Delete")
                        .param("lastName", "Test"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/medicalRecord")
                        .param("firstName", "Delete")
                        .param("lastName", "Test"))
                .andExpect(status().isNotFound());
    }
}