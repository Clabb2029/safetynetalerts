package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @GetMapping("/medicalRecords")
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordService.getAllMedicalRecords();
    }

    @PostMapping("/medicalRecords")
    public MedicalRecord createMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        return medicalRecordService.createMedicalRecord(medicalRecord);
    }

    @PutMapping("/medicalRecords/{lastName}/{firstName}")
    public MedicalRecord updateMedicalRecord(@PathVariable("lastName") String lastName,@PathVariable("firstName") String firstName, @RequestBody MedicalRecord medicalRecord) {
        return medicalRecordService.updateMedicalRecord(lastName, firstName, medicalRecord);
    }

    @DeleteMapping("/medicalRecords/{lastName}/{firstName}")
    public boolean deleteMedicalRecord(@PathVariable("lastName") String lastName,@PathVariable("firstName") String firstName){
        return medicalRecordService.deleteMedicalRecord(lastName, firstName);
    }

}
