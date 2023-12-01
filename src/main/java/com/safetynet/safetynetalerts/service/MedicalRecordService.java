package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    public List<MedicalRecord> getAllMedicalRecords(){
        return medicalRecordRepository.findAll();
    }

    public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
        return medicalRecordRepository.save(medicalRecord);
    }

    public MedicalRecord updateMedicalRecord(String lastName, String firstName, MedicalRecord medicalRecord) {
        return medicalRecordRepository.update(lastName, firstName, medicalRecord);
    }

    public boolean deleteMedicalRecord(String lastName, String firstName) {
        return medicalRecordRepository.delete(lastName, firstName);
    }
}
