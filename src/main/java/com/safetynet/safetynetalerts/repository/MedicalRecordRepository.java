package com.safetynet.safetynetalerts.repository;

import com.safetynet.safetynetalerts.model.DataModel;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Repository
public class MedicalRecordRepository {

    public static DataModel dataModel = new DataModel();

    public List<MedicalRecord> findAll() {
        return dataModel.getMedicalRecords();
    }

    public MedicalRecord save(MedicalRecord medicalRecord) {
        dataModel.addMedicalRecord(medicalRecord);
        return dataModel.getMedicalRecords().stream().filter(f -> (
                f.getFirstName().equals(medicalRecord.getFirstName())
                && f.getLastName().equals(medicalRecord.getLastName())
                && f.getBirthdate().equals(medicalRecord.getBirthdate())
                && f.getMedications().equals(medicalRecord.getMedications())
                && f.getAllergies().equals(medicalRecord.getAllergies())
        )).findAny().orElse(null);
    }

    public MedicalRecord update(String lastName, String firstName, MedicalRecord medicalRecord) {
        MedicalRecord fetchedMedicalRecord = findByFullName(lastName, firstName);
        if(!ObjectUtils.isEmpty(fetchedMedicalRecord)) {
            fetchedMedicalRecord.setBirthdate(medicalRecord.getBirthdate());
            fetchedMedicalRecord.setMedications(medicalRecord.getMedications());
            fetchedMedicalRecord.setAllergies(medicalRecord.getAllergies());
        }
        return fetchedMedicalRecord;
    }

    public MedicalRecord findByFullName(String firstName, String lastName) {
        return dataModel.getMedicalRecords().stream()
                .filter(medicalRecord -> (
                        medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName))
                ).findAny().orElse(null);
    }

    public boolean delete(String lastName, String firstName) {
        MedicalRecord fetchedMedicalRecord = findByFullName(lastName, firstName);
        if(ObjectUtils.isEmpty(fetchedMedicalRecord)){
            return false;
        } else {
            return dataModel.getMedicalRecords().remove(fetchedMedicalRecord);
        }
    }

}
