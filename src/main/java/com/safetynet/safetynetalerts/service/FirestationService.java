package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.model.Firestation;
import com.safetynet.safetynetalerts.repository.FirestationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FirestationService {

    @Autowired
    private FirestationRepository firestationRepository;

    public List<Firestation> getAllFirestations() {
        return firestationRepository.findAll();
    }

    public Firestation createFirestation(Firestation firestation) {
        return firestationRepository.save(firestation);
    }

    public Firestation updateFirestationStation(String address, Firestation firestation) {
        return firestationRepository.updateStation(address, firestation);
    }

    public boolean deleteFirestation(String address, String station) {
        return firestationRepository.delete(address, station);
    }
}
