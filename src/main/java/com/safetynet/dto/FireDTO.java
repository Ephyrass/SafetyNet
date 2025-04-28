package com.safetynet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FireDTO {
    private String stationNumber;
    private List<PersonMedicalInfoDTO> residents;
}
