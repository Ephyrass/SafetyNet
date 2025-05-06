package com.safetynet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.model.FireStation;
import com.safetynet.service.FireStationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FireStationService fireStationService;

    @BeforeEach
    public void setup() {
        // Delete the fire station if it exists
        fireStationService.deleteFireStationByAddress("123 Main St");
    }

    @Test
    public void testAddFireStation() throws Exception {
        FireStation fireStation = new FireStation();
        fireStation.setAddress("123 Main St");
        fireStation.setStation("1");

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fireStation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.address").value("123 Main St"))
                .andExpect(jsonPath("$.station").value("1"));
    }

    @Test
    public void testUpdateFireStation() throws Exception {
        FireStation fireStation = new FireStation();
        fireStation.setAddress("123 Main St");
        fireStation.setStation("1");

        mockMvc.perform(post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fireStation)));

        fireStation.setStation("2");

        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fireStation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.station").value("2"));
    }

    @Test
    public void testDeleteFireStationByAddress() throws Exception {
        FireStation fireStation = new FireStation();
        fireStation.setAddress("123 Main St");
        fireStation.setStation("1");

        mockMvc.perform(post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fireStation)));

        mockMvc.perform(delete("/firestation")
                        .param("address", "123 Main St"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/firestation")
                        .param("address", "123 Main St"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteFireStationByStationNumber() throws Exception {
        FireStation fireStation = new FireStation();
        fireStation.setAddress("123 Main St");
        fireStation.setStation("1");

        mockMvc.perform(post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fireStation)));

        mockMvc.perform(delete("/firestation")
                        .param("station", "1"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/firestation")
                        .param("station", "1"))
                .andExpect(status().isNotFound());
    }
}