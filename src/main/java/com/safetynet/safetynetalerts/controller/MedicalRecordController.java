package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class MedicalRecordController {

/*    @Autowired
    private MedicalRecordService medicalRecordService;*/

   /* @GetMapping("/medicalRecords")
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordService.getAllMedicalRecords();
    }

    @PostMapping("/medicalRecords")
    public MedicalRecord createMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        return medicalRecordService.createMedicalRecord(medicalRecord);
    }

    @PutMapping("/medicalRecords/{personFullName}")
    public MedicalRecord updateMedicalRecord(@PathVariable String personFullName, @RequestBody MedicalRecord medicalRecord) {
        return medicalRecordService.updateMedicalRecord(personFullName, medicalRecord);
    }

    @DeleteMapping("/medicalRecords/{personFullName}")
    public boolean deleteMedicalRecord(@PathVariable String personFullName){
        return medicalRecordService.deleteMedicalRecord(personFullName);
    }*/

}
