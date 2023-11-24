package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MedicalRecordService {

    /*@Autowired
    private MedicalRecordRepository medicalRecordRepository;*/

   /* public List<MedicalRecord> getAllMedicalRecords(){
        return medicalRecordRepository.findAll();
    }

    public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
        return medicalRecordRepository.save(medicalRecord);
    }

    public MedicalRecord updateMedicalRecord(String fullName, MedicalRecord medicalRecord) {
        return medicalRecordRepository.update(fullName, medicalRecord);
    }

    public boolean deleteMedicalRecord(String fullName) {
        return medicalRecordRepository.delete(fullName);
    }*/
}
