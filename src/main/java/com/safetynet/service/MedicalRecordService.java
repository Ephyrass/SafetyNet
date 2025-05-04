package com.safetynet.service;

import com.safetynet.dto.PersonInfoDTO;
import com.safetynet.model.MedicalRecord;
import com.safetynet.model.Person;
import com.safetynet.utils.AgeCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalRecordService {

    private final DataService dataService;

    public List<MedicalRecord> getAllMedicalRecords() {
        return dataService.getDataStore().getMedicalRecords();
    }

    public MedicalRecord getMedicalRecord(String firstName, String lastName) {
        log.debug("Searching for medical record for: {} {}", firstName, lastName);
        return getAllMedicalRecords().stream()
                .filter(mr -> mr.getFirstName().equals(firstName) && mr.getLastName().equals(lastName))
                .findFirst()
                .orElse(null);
    }


    public PersonInfoDTO buildMedicalInfoDTO(Person person) {
        log.debug("Building medical information DTO for: {} {}",
                person.getFirstName(), person.getLastName());

        MedicalRecord medicalRecord = getMedicalRecord(person.getFirstName(), person.getLastName());

        if (medicalRecord != null) {
            return new PersonInfoDTO(
                    person.getFirstName(),
                    person.getLastName(),
                    person.getPhone(),
                    AgeCalculator.calculateAge(medicalRecord.getBirthdate()),
                    medicalRecord.getMedications(),
                    medicalRecord.getAllergies()
            );
        }
        return null;
    }


    public List<PersonInfoDTO> getMedicalInfoByAddress(String address, PersonService personService) {
        log.debug("Retrieving medical information for address: {}", address);

        List<Person> residents = personService.getPersonsByAddress(address);
        List<PersonInfoDTO> residentInfos = new ArrayList<>();

        for (Person person : residents) {
            PersonInfoDTO info = buildMedicalInfoDTO(person);
            if (info != null) {
                residentInfos.add(info);
            }
        }

        return residentInfos;
    }
    
    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        log.info("Adding new medical record for: {} {}",
                medicalRecord.getFirstName(), medicalRecord.getLastName());
        dataService.getDataStore().getMedicalRecords().add(medicalRecord);
        dataService.saveDataToFile();
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
                dataService.saveDataToFile();
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
        boolean removed = medicalRecords.removeIf(mr ->
                mr.getFirstName().equals(firstName) && mr.getLastName().equals(lastName));
        if (removed) {
            dataService.saveDataToFile();
        }
        return removed;
    }
}
