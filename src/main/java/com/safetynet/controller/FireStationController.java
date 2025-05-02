package com.safetynet.controller;

import com.safetynet.model.FireStation;
import com.safetynet.service.FireStationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FireStationController {

    private final FireStationService fireStationService;

    /**
     * Adds a new fire station mapping.
     *
     * @param fireStation The mapping to add
     * @return The added mapping
     */
    @PostMapping("/firestation")
    public ResponseEntity<FireStation> addFireStation(@RequestBody FireStation fireStation) {
        try {
            log.debug("Adding a new fire station mapping: {} - Station #{}",
                    fireStation.getAddress(), fireStation.getStation());

            // Check if mapping already exists
            FireStation existingFireStation = fireStationService.getStationByAddress(fireStation.getAddress());
            if (existingFireStation != null) {
                log.error("Mapping for address {} already exists", fireStation.getAddress());
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            FireStation addedFireStation = fireStationService.addFireStation(fireStation);
            log.info("New fire station mapping added: {} - Station #{}",
                    fireStation.getAddress(), fireStation.getStation());
            return ResponseEntity.status(HttpStatus.CREATED).body(addedFireStation);
        } catch (Exception e) {
            log.error("Error adding fire station mapping: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Updates the station number for an existing address.
     *
     * @param fireStation The mapping to update
     * @return The updated mapping or an error if not found
     */
    @PutMapping("/firestation")
    public ResponseEntity<FireStation> updateFireStation(@RequestBody FireStation fireStation) {
        try {
            log.debug("Updating fire station number for address: {}", fireStation.getAddress());

            FireStation updatedFireStation = fireStationService.updateFireStation(fireStation);
            if (updatedFireStation == null) {
                log.error("Mapping not found for address: {}", fireStation.getAddress());
                return ResponseEntity.notFound().build();
            }

            log.info("Fire station mapping updated: {} - Station #{}",
                    updatedFireStation.getAddress(), updatedFireStation.getStation());
            return ResponseEntity.ok(updatedFireStation);
        } catch (Exception e) {
            log.error("Error updating fire station mapping: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Deletes a fire station mapping.
     *
     * @param address The address to delete (optional)
     * @param stationNumber The station number to delete (optional)
     * @return An empty response with HTTP status
     */
    @DeleteMapping("/firestation")
    public ResponseEntity<Void> deleteFireStation(
            @RequestParam(required = false) String address,
            @RequestParam(name = "station", required = false) String stationNumber) {
        try {
            log.debug("Deleting fire station mapping: address={}, station={}", address, stationNumber);

            // Check that at least one parameter is provided
            if (address == null && stationNumber == null) {
                log.error("No parameters provided for deletion");
                return ResponseEntity.badRequest().build();
            }

            boolean deleted;
            if (address != null && stationNumber == null) {
                // Delete by address
                deleted = fireStationService.deleteFireStationByAddress(address);
            } else if (address == null) {
                // Delete by station number
                deleted = fireStationService.deleteFireStationByStationNumber(stationNumber);
            } else {
                // Delete by address and station number
                deleted = fireStationService.deleteFireStation(address, stationNumber);
            }

            if (!deleted) {
                log.error("Mapping not found for deletion");
                return ResponseEntity.notFound().build();
            }

            log.info("Fire station mapping successfully deleted");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting fire station mapping: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
