package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.model.Firestation;
import com.safetynet.safetynetalerts.service.FirestationService;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class FirestationController {

    @Autowired
    private FirestationService firestationService;

    private static final Logger logger = LogManager.getLogger(FirestationController.class);

    @GetMapping("/firestations")
    public ResponseEntity<List<Firestation>> getFirestations(){
        List<Firestation> returnedFirestationList = firestationService.getAllFirestations();
        if(!returnedFirestationList.isEmpty()) {
            logger.info("Firestation list fetched successfully.");
            return ResponseEntity.ok(returnedFirestationList);
        } else {
            logger.error("There was an error when fetching the firestation list.");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/firestations")
    public ResponseEntity<Firestation> createFirestation(@RequestBody Firestation firestation) {
        Firestation returnedFirestation = firestationService.createFirestation(firestation);
        if(returnedFirestation != null) {
            logger.info("Firestation created successfully.");
            return ResponseEntity.ok(returnedFirestation);
        } else {
            logger.error("There was an error when creating the firestation.");
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/firestations/{address}")
    public ResponseEntity<Firestation> updateFirestationStation(@PathVariable String address, @RequestBody Firestation firestation) {
        Firestation returnedFirestation = firestationService.updateFirestationStation(address, firestation);
        if (returnedFirestation != null) {
            logger.info("Firestation updated successfully.");
            return ResponseEntity.ok(returnedFirestation);
        } else {
            logger.error("There was an error when updating the firestation.");
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/firestations/{address}/{station}")
    public ResponseEntity<Boolean> deleteFirestation(@PathVariable String address, @PathVariable String station){
        boolean isFirestationDeleted = firestationService.deleteFirestation(address, station);
        if (isFirestationDeleted) {
            logger.info("Firestation deleted successfully.");
            return ResponseEntity.ok(isFirestationDeleted);
        } else {
            logger.error("There was an error when deleting the firestation.");
            return ResponseEntity.notFound().build();
        }
    }
}
