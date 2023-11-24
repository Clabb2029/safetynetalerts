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

  /*  @GetMapping("/firestations")
    public List<Firestation> getFirestations(){
        return firestationService.getFirestations();
    }

    @PostMapping("/firestations")
    public Firestation createFirestation(@RequestBody Firestation firestation) {
        return firestationService.createFirestation(firestation);
    }

    @PutMapping("/firestations/{stationNumber}")
    public Firestation updateFirestation(@PathVariable String stationNumber, @RequestBody Firestation firestation) {
        return firestationService.updateFirestation(stationNumber, firestation);
    }

    @DeleteMapping("/firestations/{stationNumber}")
    public void deleteFirestation(@PathVariable String stationNumber){
        return firestationService.deleteFirestation(stationNumber);
    }*/




}
