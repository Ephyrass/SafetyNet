package com.safetynet.service;

import com.safetynet.model.FireStation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FireStationService {

    private final DataLoaderService dataLoaderService;



    public List<FireStation> getAllFireStations() {
        return dataLoaderService.getDataStore().getFireStations();
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

    public FireStation addFireStation(FireStation fireStation) {
        log.info("add new fire station: {} - Station {}",
                fireStation.getAddress(), fireStation.getStation());
        dataLoaderService.getDataStore().getFireStations().add(fireStation);
        return fireStation;
    }

    public FireStation updateFireStation(FireStation fireStation) {
        log.info("update number of fire station: {}", fireStation.getAddress());

        List<FireStation> fireStations = getAllFireStations();
        for (int i = 0; i < fireStations.size(); i++) {
            FireStation fs = fireStations.get(i);
            if (fs.getAddress().equals(fireStation.getAddress())) {
                fireStations.set(i, fireStation);
                return fireStation;
            }
        }

        log.error("adress not found for this firestation: {}", fireStation.getAddress());
        return null;
    }

    public boolean deleteFireStation(String address) {
        log.info("delete fire station adress : {}", address);

        List<FireStation> fireStations = getAllFireStations();
        return fireStations.removeIf(fs -> fs.getAddress().equals(address));
    }

    public boolean deleteFireStationByStation(String station) {
        log.info("Delete fires station: {}", station);

        List<FireStation> fireStations = getAllFireStations();
        return fireStations.removeIf(fs -> fs.getStation().equals(station));
    }
}