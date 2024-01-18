package com.safetynet.safetynetalerts.repository;

import com.safetynet.safetynetalerts.DTO.FirestationPersonDTO;
import com.safetynet.safetynetalerts.model.DataModel;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
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

    public List<MedicalRecord> findAllByName(List<FirestationPersonDTO> firestationPersonDTOList) {
        List<MedicalRecord> medicalRecordList = new ArrayList<>();
        dataModel.getMedicalRecords().forEach(medicalRecord -> {
            firestationPersonDTOList.forEach(person -> {
                if (medicalRecord.getFirstName().equals(person.getFirstName()) && medicalRecord.getLastName().equals(person.getLastName())) {
                    medicalRecordList.add(medicalRecord);
                }
            });
        });
        return medicalRecordList;
    }

    public List<MedicalRecord> findAllByNames(List<Person> personList) {
        List<MedicalRecord> medicalRecordListCopy = (List<MedicalRecord>)((ArrayList<MedicalRecord>)dataModel.getMedicalRecords()).clone();
        List<MedicalRecord> returnedMedicalRecordList = new ArrayList<>();
        for(int i = 0 ; i < personList.size() ; i++) {
            int finalI = i;
            MedicalRecord filteredMedicalRecord = medicalRecordListCopy.stream().filter(medicalRecord -> (
                    medicalRecord.getFirstName().equals(personList.get(finalI).getFirstName())
                    && medicalRecord.getLastName().equals(personList.get(finalI).getLastName())
                    )).findAny().orElse(null);
            returnedMedicalRecordList.add(filteredMedicalRecord);
            medicalRecordListCopy.remove(filteredMedicalRecord);
        }
        return returnedMedicalRecordList;
    }

}









