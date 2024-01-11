package com.safetynet.safetynetalerts.repository;

import com.safetynet.safetynetalerts.model.DataModel;
import com.safetynet.safetynetalerts.model.Firestation;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FirestationRepository {

    public static DataModel dataModel = new DataModel();

    public List<Firestation> findAll() {
        return dataModel.getFirestations();
    }

    public Firestation save(Firestation firestation) {
        dataModel.addFirestation(firestation);
        return dataModel.getFirestations().stream().filter(f -> (
                f.getAddress().equals(firestation.getAddress())
                && f.getStation().equals(firestation.getStation())
        )).findAny().orElse(null);
    }

    public Firestation updateStation(String address, Firestation firestation) {
        Firestation fetchedFirestation = dataModel.getFirestations().stream().filter(f -> (
                f.getAddress().equals(address))).findAny().orElse(null);
        if (!ObjectUtils.isEmpty(fetchedFirestation)) {
            fetchedFirestation.setStation(firestation.getStation());
        }
        return fetchedFirestation;
    }

    public boolean delete(String address, String station) {
        Firestation fetchedFirestation = dataModel.getFirestations().stream().filter(f -> (
                f.getAddress().equals(address) && f.getStation().equals(station))).findAny().orElse(null);
        if(ObjectUtils.isEmpty(fetchedFirestation)) {
            return false;
        } else {
            return dataModel.getFirestations().remove(fetchedFirestation);
        }
    }

    public List<String> findAllByStationNumber(String stationNumber) {
        List<String> firestationAddressList = new ArrayList<>();
        dataModel.getFirestations().forEach(firestation -> {
            if (firestation.getStation().equals(stationNumber)) {
                firestationAddressList.add(firestation.getAddress());
            }
        });
        return firestationAddressList;
    }

    public Firestation findOneByAddress(String address) {
        return dataModel.getFirestations().stream().filter(
                firestation -> (
                        firestation.getAddress().equals(address)
                )
        ).findAny().orElse(null);
    }
}