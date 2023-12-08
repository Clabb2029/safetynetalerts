package com.safetynet.safetynetalerts.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class DataModel {

    public static List<Person> persons = new ArrayList<Person>();
    public static List<Firestation> firestations = new ArrayList<Firestation>();
    public static List<MedicalRecord> medicalRecords = new ArrayList<MedicalRecord>();

    public List<Person> getPersons(){
        return persons;
    }

    public void setPersons(List<Person> personsList) {
        persons = personsList;
    }

    public void addPerson(Person person) {
        persons.add(person);
    }

    public List<Firestation> getFirestations(){
        return firestations;
    }

    public void setFirestations(List<Firestation> firestationsList) {
        firestations = firestationsList;
    }

    public void addFirestation(Firestation firestation) {
        firestations.add(firestation);
    }


    public List<MedicalRecord> getMedicalRecords(){
        return medicalRecords;
    }

    public void setMedicalRecords(List<MedicalRecord> medicalRecordsList) {
        medicalRecords = medicalRecordsList;
    }

    public void addMedicalRecord(MedicalRecord medicalRecord) {
        medicalRecords.add(medicalRecord);
    }

}
