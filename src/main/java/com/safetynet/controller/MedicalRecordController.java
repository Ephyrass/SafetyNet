package com.safetynet.controller;

import com.safetynet.model.MedicalRecord;
import com.safetynet.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @PostMapping("/medicalRecord")
    public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        log.info("POST request received to add medical record: {} {}",
                medicalRecord.getFirstName(), medicalRecord.getLastName());

        MedicalRecord addedRecord = medicalRecordService.addMedicalRecord(medicalRecord);
        return new ResponseEntity<>(addedRecord, HttpStatus.CREATED);
    }

    @PutMapping("/medicalRecord")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        log.info("PUT request received to update medical record: {} {}",
                medicalRecord.getFirstName(), medicalRecord.getLastName());

        MedicalRecord updatedRecord = medicalRecordService.updateMedicalRecord(medicalRecord);

        if (updatedRecord == null) {
            log.error("Medical record not found for updating: {} {}",
                    medicalRecord.getFirstName(), medicalRecord.getLastName());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(updatedRecord, HttpStatus.OK);
    }

    @DeleteMapping("/medicalRecord")
    public ResponseEntity<Void> deleteMedicalRecord(
            @RequestParam String firstName,
            @RequestParam String lastName) {
        log.info("DELETE request received to delete medical record: {} {}",
                firstName, lastName);

        boolean isDeleted = medicalRecordService.deleteMedicalRecord(firstName, lastName);

        if (!isDeleted) {
            log.error("Medical record not found for deleting: {} {}", firstName, lastName);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
