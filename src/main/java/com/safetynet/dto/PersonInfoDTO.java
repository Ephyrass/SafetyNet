package com.safetynet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PersonInfoDTO {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String email;
    private Integer age;
    private List<String> medications;
    private List<String> allergies;

    /* Using surcharged constructors to create different instances of PersonInfoDTO
       depending on the information available. */

    // Constructor for basic information
    public PersonInfoDTO(String firstName, String lastName, String address, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    // Constructor for medical information
    public PersonInfoDTO(String firstName, String lastName, String phone, int age, List<String> medications, List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.age = age;
        this.medications = medications;
        this.allergies = allergies;
    }
    // Constructor for full information
    public PersonInfoDTO(String firstName, String lastName, String address, String phone, String email, Integer age, List<String> medications, List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.age = age;
        this.medications = medications;
        this.allergies = allergies;
    }
}
