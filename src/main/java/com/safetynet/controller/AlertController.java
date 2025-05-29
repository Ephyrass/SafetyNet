package com.safetynet.controller;

import com.safetynet.dto.*;
import com.safetynet.model.MedicalRecord;
import com.safetynet.model.Person;
import com.safetynet.service.FireStationService;
import com.safetynet.service.MedicalRecordService;
import com.safetynet.service.PersonService;
import com.safetynet.utils.AgeCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AlertController {

    private final PersonService personService;
    private final FireStationService fireStationService;
    private final MedicalRecordService medicalRecordService;

    /**
     * Endpoint to get the coverage of a fire station.
     *
     * @param stationNumber The number of the fire station.
     * @return A response entity containing the coverage information.
     */

    @GetMapping("/firestation")
    public ResponseEntity<FireStationCoverageDTO> getFireStationCoverage(@RequestParam String stationNumber) {
        try {
            log.debug("Coverage request for station: {}", stationNumber);

            List<Person> coveredPersons = fireStationService.getPersonsCoveredByStation(stationNumber, personService);

            List<PersonInfoDTO> personInfos = new ArrayList<>();
            int adultCount = 0;
            int childCount = 0;

            for (Person person : coveredPersons) {
                PersonInfoDTO personInfo = new PersonInfoDTO(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(),
                        person.getPhone(),
                        person.getEmail()

                );
                personInfos.add(personInfo);

                MedicalRecord medicalRecord = medicalRecordService.getMedicalRecord(
                        person.getFirstName(), person.getLastName());

                if (medicalRecord != null) {
                    if (AgeCalculator.isChild(medicalRecord.getBirthdate())) {
                        childCount++;
                    } else {
                        adultCount++;
                    }
                }
            }

            FireStationCoverageDTO response = new FireStationCoverageDTO(personInfos, adultCount, childCount);
            log.info("Station {} coverage: {} adults, {} children",
                    stationNumber, adultCount, childCount);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving station coverage: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    /**
     * Endpoint to get the child alert for a specific address.
     *
     * @param address The address to check for child alerts.
     * @return A response entity containing the list of children and their household members.
     */

    @GetMapping("/childAlert")
    public ResponseEntity<List<ChildAlertDTO>> getChildAlert(@RequestParam String address) {
        try {
            log.debug("Child alert request for address: {}", address);

            List<Person> residents = personService.getPersonsByAddress(address);
            List<ChildAlertDTO> children = new ArrayList<>();
            List<PersonInfoDTO> adults = new ArrayList<>();

            for (Person person : residents) {
                MedicalRecord medicalRecord = medicalRecordService.getMedicalRecord(
                        person.getFirstName(), person.getLastName());

                PersonInfoDTO personInfo = new PersonInfoDTO(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(),
                        person.getPhone(),
                        person.getEmail()
                );

                if (medicalRecord != null) {
                    if (AgeCalculator.isChild(medicalRecord.getBirthdate())) {
                        ChildAlertDTO child = new ChildAlertDTO();
                        child.setFirstName(person.getFirstName());
                        child.setLastName(person.getLastName());
                        child.setAge(AgeCalculator.calculateAge(medicalRecord.getBirthdate()));
                        children.add(child);
                    } else {
                        adults.add(personInfo);
                    }
                }
            }
            /*
             * Set the adult list in each child alert object
             * This is done to avoid sending the same list multiple times
             */
            for (ChildAlertDTO child : children) {
                child.setHouseholdMembers(adults);
            }

            log.info("Child alert for address {}: {} children found", address, children.size());

            return ResponseEntity.ok(children);
        } catch (Exception e) {
            log.error("Error retrieving child alert: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint to get the phone alert for a specific fire station.
     *
     * @param firestation The fire station number to check for phone alerts.
     * @return A response entity containing the phone alert data with station number and phone numbers.
     */
    @GetMapping("/phoneAlert")
    public ResponseEntity<PhoneAlertDTO> getPhoneAlert(@RequestParam String firestation) {
        try {
            log.debug("Phone alert request for station: {}", firestation);

            List<Person> coveredPersons = fireStationService.getPersonsCoveredByStation(firestation, personService);

            List<String> uniquePhoneNumbers = coveredPersons.stream()
                    .map(Person::getPhone)
                    .distinct()
                    .collect(Collectors.toList());

            PhoneAlertDTO response = new PhoneAlertDTO(firestation, uniquePhoneNumbers);

            log.info("Phone alert for station {}: {} unique numbers",
                    firestation, uniquePhoneNumbers.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving phone alert: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    /**
     * Endpoint to get fire information for a specific address.
     *
     * @param address The address to check for fire information.
     * @return A response entity containing the fire station number and residents' information.
     */
    @GetMapping("/fire")
    public ResponseEntity<FireDTO> getFireInfo(@RequestParam String address) {
        try {
            log.debug("Fire info request for address: {}", address);

            String stationNumber = fireStationService.getStationNumberByAddress(address);

            List<Person> residents = personService.getPersonsByAddress(address);
            List<PersonInfoDTO> residentInfos = new ArrayList<>();

            for (Person person : residents) {
                MedicalRecord medicalRecord = medicalRecordService.getMedicalRecord(
                        person.getFirstName(), person.getLastName());

                if (medicalRecord != null) {
                    PersonInfoDTO residentInfo = new PersonInfoDTO(
                            person.getFirstName(),
                            person.getLastName(),
                            person.getPhone(),
                            AgeCalculator.calculateAge(medicalRecord.getBirthdate()),
                            medicalRecord.getMedications(),
                            medicalRecord.getAllergies()
                    );
                    residentInfos.add(residentInfo);
                }
            }

            FireDTO response = new FireDTO(stationNumber, residentInfos);

            log.info("Fire info for address {}: station {} with {} residents",
                    address, stationNumber, residentInfos.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving fire info: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint to get household information for stations in case of flood.
     *
     * @param stations A list of fire station numbers.
     * @return A response entity containing households grouped by address for the specified stations.
     */
    @GetMapping("/flood/stations")
    public ResponseEntity<List<FloodAlertDTO>> getFloodStations(@RequestParam List<String> stations) {
        try {
            log.debug("Flood stations request for stations: {}", stations);

            List<FloodAlertDTO> response = new ArrayList<>();

            for (String station : stations) {
                List<String> addresses = fireStationService.getAddressesByStation(station);

                for (String address : addresses) {
                    List<PersonInfoDTO> residentInfos =
                            medicalRecordService.getMedicalInfoByAddress(address, personService);

                    FloodAlertDTO householdInfo = new FloodAlertDTO(address, residentInfos);
                    response.add(householdInfo);
                }
            }

            log.info("Flood info for stations {}: {} households found",
                    stations, response.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving flood stations info: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    /**
        * Endpoint to get person information by last name.
        *
        * @param lastName The last name of the person to search for.
        * @return A response entity containing the person's information.
        */
    @GetMapping("/personInfoLastName")
    public ResponseEntity<List<PersonInfoDTO>> getPersonInfo(@RequestParam String lastName) {
        try {
            log.debug( "Person info request for last name: {}", lastName);

            List<Person> persons = personService.getPersonsByLastName(lastName);
            List<PersonInfoDTO> personInfoList = new ArrayList<>();

            for (Person person : persons) {
                MedicalRecord medicalRecord = medicalRecordService.getMedicalRecord(
                        person.getFirstName(), person.getLastName());

                if (medicalRecord != null) {
                    PersonInfoDTO personInfo = new PersonInfoDTO(
                            person.getFirstName(),
                            person.getLastName(),
                            person.getAddress(),
                            person.getEmail(),
                            person.getPhone(),
                            AgeCalculator.calculateAge(medicalRecord.getBirthdate()),
                            medicalRecord.getMedications(),
                            medicalRecord.getAllergies()
                    );
                    personInfoList.add(personInfo);
                }
            }

            log.info("Person info for last name {}: {} persons found",
                    lastName, personInfoList.size());

            return ResponseEntity.ok(personInfoList);
        } catch (Exception e) {
            log.error("Error retrieving person info: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    /**
        * Endpoint to get community email addresses for a specific city.
        *
        * @param city The city to check for community email addresses.
        * @return A response entity containing the community email addresses.
        */
    @GetMapping("/communityEmail")
    public ResponseEntity<CommunityEmailDTO> getCommunityEmails(@RequestParam String city) {
        try {
            log.debug("Community email request for city: {}", city);

            List<String> emails = personService.getAllPersons().stream()
                    .filter(person -> person.getCity().equalsIgnoreCase(city))
                    .map(Person::getEmail)
                    .distinct()
                    .collect(Collectors.toList());

            CommunityEmailDTO response = new CommunityEmailDTO(city, emails);

            log.info("Community email for city {}: {} emails found",
                    city, emails.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving community emails: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
