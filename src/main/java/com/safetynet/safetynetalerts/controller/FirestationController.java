package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.model.Firestation;
import com.safetynet.safetynetalerts.service.FirestationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FirestationController {

    @Autowired
    private FirestationService firestationService;

    @GetMapping("/firestations")
    public List<Firestation> getFirestations(){
        return firestationService.getAllFirestations();
    }

    @PostMapping("/firestations")
    public Firestation createFirestation(@RequestBody Firestation firestation) {
        return firestationService.createFirestation(firestation);
    }

    @PutMapping("/firestations/{address}")
    public Firestation updateFirestationStation(@PathVariable String address, @RequestBody Firestation firestation) {
        return firestationService.updateFirestationStation(address, firestation);
    }

    @DeleteMapping("/firestations/{address}/{station}")
    public boolean deleteFirestation(@PathVariable String address, @PathVariable String station){
        return firestationService.deleteFirestation(address, station);
    }
}
