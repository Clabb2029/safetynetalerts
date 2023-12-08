package com.safetynet.safetynetalerts.repository;

import com.safetynet.safetynetalerts.model.DataModel;
import com.safetynet.safetynetalerts.model.Firestation;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Repository
public class FirestationRepository {

    public static DataModel dataModel = new DataModel();

    public List<Firestation> findAll() {
        return dataModel.getFirestations();
    }

    public Firestation save(Firestation firestation) {
        dataModel.addFirestation(firestation);
        return firestation;
    }

    public Firestation updateStation(String address, Firestation firestation) {
        Firestation fetchedFirestation = dataModel.getFirestations().stream().filter(f -> (
                f.getAddress().equals(address))).findAny().orElseThrow();
        if(ObjectUtils.isEmpty(fetchedFirestation)) {
            return null;
        } else {
            fetchedFirestation.setStation(firestation.getStation());
            return fetchedFirestation;
        }
    }

    public boolean delete(String address, String station) {
        Firestation fetchedFirestation = dataModel.getFirestations().stream().filter(f -> (
                f.getAddress().equals(address) && f.getStation().equals(station))).findAny().orElseThrow();
        if(ObjectUtils.isEmpty(fetchedFirestation)) {
            return false;
        } else {
            return dataModel.getFirestations().remove(fetchedFirestation);
        }
    }
}
