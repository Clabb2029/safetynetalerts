package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.model.Firestation;
import com.safetynet.safetynetalerts.service.FirestationService;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class FirestationController {

    @Autowired
    private FirestationService firestationService;

    private static final Logger logger = LogManager.getLogger(FirestationController.class);

    @GetMapping("/firestations")
    public List<Firestation> getFirestations(){
        List<Firestation> returnedFirestationsArray = firestationService.getAllFirestations();
        if(!returnedFirestationsArray.isEmpty()) {
            logger.info("Firestations list fetched successfully.");
        } else {
            logger.error("There was an error when fetching the firestations list.");
        }
        return returnedFirestationsArray;
    }

    @PostMapping("/firestations")
    public Firestation createFirestation(@RequestBody Firestation firestation) {
        Firestation returnedFirestation = firestationService.createFirestation(firestation);
        if(returnedFirestation != null) {
            logger.info("Firestation created successfully.");
        } else {
            logger.error("There was an error when creating the firestation.");
        }
        return returnedFirestation;
    }

    @PutMapping("/firestations/{address}")
    public Firestation updateFirestationStation(@PathVariable String address, @RequestBody Firestation firestation) {
        Firestation returnedFirestation = firestationService.updateFirestationStation(address, firestation);
        if (returnedFirestation != null) {
            logger.info("Firestation updated successfully.");
        } else {
            logger.error("There was an error when updating the firestation.");
        }
        return returnedFirestation;
    }

    @DeleteMapping("/firestations/{address}/{station}")
    public boolean deleteFirestation(@PathVariable String address, @PathVariable String station){
        boolean isFirestationDeleted = firestationService.deleteFirestation(address, station);
        if (isFirestationDeleted) {
            logger.info("Firestation deleted successfully.");
        } else {
            logger.error("There was an error when deleting the firestation.");
        }
        return isFirestationDeleted;
    }
}
