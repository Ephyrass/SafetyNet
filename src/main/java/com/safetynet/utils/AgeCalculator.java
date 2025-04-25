package com.safetynet.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class AgeCalculator {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public static int calculateAge(String birthDate) {
        LocalDate birthLocalDate = LocalDate.parse(birthDate, formatter);
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthLocalDate, currentDate).getYears();
    }

    public static boolean isChild(String birthDate) {
        return calculateAge(birthDate) <= 18;
    }
}