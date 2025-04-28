package com.safetynet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FireStationCoverageDTO {
    private List<PersonInfoDTO> persons;
    private int adultCount;
    private int childCount;
}