package com.safetynet.service;

import com.safetynet.model.FireStation;
import com.safetynet.model.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FireStationService {

    private final DataService dataService;



    public List<FireStation> getAllFireStations() {
        return dataService.getDataStore().getFireStations();
    }

    public List<FireStation> getFireStationsByStation(String station) {
        log.debug("Get Fire station by number: {}", station);
        return getAllFireStations().stream()
                .filter(fs -> fs.getStation().equals(station))
                .collect(Collectors.toList());
    }
    

    public List<String> getAddressesByStation(String station) {
        log.debug("Get address by station: {}", station);
        return getFireStationsByStation(station).stream()
                .map(FireStation::getAddress)
                .collect(Collectors.toList());
    }

    public FireStation getStationByAddress(String address) {
        log.debug("Get Fire station by address: {}", address);
        return getAllFireStations().stream()
                .filter(fs -> fs.getAddress().equals(address))
                .findFirst()
                .orElse(null);
    }
    public String getStationNumberByAddress(String address) {
        log.debug("Get Fire station number by address: {}", address);
        return getStationByAddress(address).getStation();
    }


    public List<Person> getPersonsCoveredByStation(String stationNumber, PersonService personService) {
        log.debug("Get persons covered by station: {}", stationNumber);
        List<String> addresses = getAddressesByStation(stationNumber);
        List<Person> coveredPersons = new ArrayList<>();

        for (String address : addresses) {
            coveredPersons.addAll(personService.getPersonsByAddress(address));
        }

        return coveredPersons;
    }

    public FireStation addFireStation(FireStation fireStation) {
        log.info("Adding new fire station mapping: {} - Station #{}",
                fireStation.getAddress(), fireStation.getStation());
        dataService.getDataStore().getFireStations().add(fireStation);
        dataService.saveDataToFile();
        return fireStation;
    }

    public FireStation updateFireStation(FireStation fireStation) {
        log.info("Updating fire station number for address: {}", fireStation.getAddress());

        List<FireStation> fireStations = getAllFireStations();
        for (int i = 0; i < fireStations.size(); i++) {
            FireStation fs = fireStations.get(i);
            if (fs.getAddress().equals(fireStation.getAddress())) {
                fireStations.set(i, fireStation);
                dataService.saveDataToFile();
                return fireStation;
            }
        }

        log.error("Mapping not found for address: {}", fireStation.getAddress());
        return null;
    }

    public boolean deleteFireStationByAddress(String address) {
        log.info("Deleting mapping by address: {}", address);
        List<FireStation> fireStations = getAllFireStations();
        boolean removed = fireStations.removeIf(fs -> fs.getAddress().equals(address));
        if (removed) {
            dataService.saveDataToFile();
        }
        return removed;
    }

    public boolean deleteFireStationByStationNumber(String stationNumber) {
        log.info("Deleting mappings by station number: {}", stationNumber);
        List<FireStation> fireStations = getAllFireStations();
        boolean removed = fireStations.removeIf(fs -> fs.getStation().equals(stationNumber));
        if (removed) {
            dataService.saveDataToFile();
        }
        return removed;
    }

    public boolean deleteFireStation(String address, String stationNumber) {
        log.info("Deleting mapping by address and station number: {} - Station #{}",
                address, stationNumber);
        List<FireStation> fireStations = getAllFireStations();
        boolean removed = fireStations.removeIf(
                fs -> fs.getAddress().equals(address) && fs.getStation().equals(stationNumber));
        if (removed) {
            dataService.saveDataToFile();
        }
        return removed;
    }
}

