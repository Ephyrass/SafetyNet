package com.safetynet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetFireStationCoverage() throws Exception {
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.persons").exists())
                .andExpect(jsonPath("$.persons").isArray())
                .andExpect(jsonPath("$.adultCount").isNumber())
                .andExpect(jsonPath("$.childCount").isNumber());
    }

    @Test
    public void testGetChildAlert() throws Exception {
        mockMvc.perform(get("/childAlert")
                        .param("address", "1509 Culver St")) // Address with children
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetPhoneAlert() throws Exception {
        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stationNumber").value("3"))
                .andExpect(jsonPath("$.phoneNumbers").isArray());
    }

    @Test
    public void testGetFireInfo() throws Exception {
        mockMvc.perform(get("/fire")
                        .param("address", "112 Steppes Pl"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stationNumber").exists())
                .andExpect(jsonPath("$.residents").isArray());
    }

    @Test
    public void testGetFloodStations() throws Exception {
        mockMvc.perform(get("/flood/stations")
                        .param("stations", "3")
                        .param("stations", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*].address").exists())
                .andExpect(jsonPath("$[*].residents").exists());
    }

    @Test
    public void testGetPersonInfo() throws Exception {
        mockMvc.perform(get("/personInfoLastName")
                        .param("lastName", "Boyd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*].firstName").exists())
                .andExpect(jsonPath("$[*].lastName").exists());
    }

    @Test
    public void testGetCommunityEmails() throws Exception {
        mockMvc.perform(get("/communityEmail")
                        .param("city", "Culver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Culver"))
                .andExpect(jsonPath("$.emails").isArray())
                .andExpect(jsonPath("$.emails", not(empty())));
    }


}