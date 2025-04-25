package com.safetynet.service;

import com.safetynet.model.MedicalRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalRecordService {

    private final DataLoaderService dataLoaderService;

    public List<MedicalRecord> getAllMedicalRecords() {
        return dataLoaderService.getDataStore().getMedicalRecords();
    }

    public MedicalRecord getMedicalRecord(String firstName, String lastName) {
        log.debug("Searching for medical record for: {} {}", firstName, lastName);
        return getAllMedicalRecords().stream()
                .filter(mr -> mr.getFirstName().equals(firstName) && mr.getLastName().equals(lastName))
                .findFirst()
                .orElse(null);
    }

    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        log.info("Adding new medical record for: {} {}",
                medicalRecord.getFirstName(), medicalRecord.getLastName());
        dataLoaderService.getDataStore().getMedicalrecords().add(medicalRecord);
        return medicalRecord;
    }

    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {
        log.info("Updating medical record for: {} {}",
                medicalRecord.getFirstName(), medicalRecord.getLastName());

        List<MedicalRecord> medicalRecords = getAllMedicalRecords();
        for (int i = 0; i < medicalRecords.size(); i++) {
            MedicalRecord mr = medicalRecords.get(i);
            if (mr.getFirstName().equals(medicalRecord.getFirstName()) &&
                    mr.getLastName().equals(medicalRecord.getLastName())) {
                medicalRecords.set(i, medicalRecord);
                return medicalRecord;
            }
        }

        log.error("Medical record not found for: {} {}",
                medicalRecord.getFirstName(), medicalRecord.getLastName());
        return null;
    }

    public boolean deleteMedicalRecord(String firstName, String lastName) {
        log.info("Deleting medical record for: {} {}", firstName, lastName);

        List<MedicalRecord> medicalRecords = getAllMedicalRecords();
        return medicalRecords.removeIf(mr ->
                mr.getFirstName().equals(firstName) && mr.getLastName().equals(lastName));
    }
}
