package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.service.MedicalRecordService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    private static final Logger logger = LogManager.getLogger(MedicalRecordController.class);

    @GetMapping("/medicalRecords")
    public ResponseEntity<List<MedicalRecord>> getAllMedicalRecords() {
        List<MedicalRecord> returnedMedicalRecordList = medicalRecordService.getAllMedicalRecords();
        if(!returnedMedicalRecordList.isEmpty()) {
            logger.info("Medical record list fetched successfully.");
            return ResponseEntity.ok(returnedMedicalRecordList);
        } else {
            logger.error("There was an error when fetching the medical record list.");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/medicalRecords")
    public ResponseEntity<MedicalRecord> createMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        MedicalRecord returnedMedicalRecord = medicalRecordService.createMedicalRecord(medicalRecord);
        if(returnedMedicalRecord != null) {
            logger.info("Medical record created successfully.");
            return ResponseEntity.ok(returnedMedicalRecord);
        } else {
            logger.error("There was an error when creating the medical record.");
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/medicalRecords/{lastName}/{firstName}")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable("lastName") String lastName,@PathVariable("firstName") String firstName, @RequestBody MedicalRecord medicalRecord) {
        MedicalRecord returnedMedicalRecord = medicalRecordService.updateMedicalRecord(lastName, firstName, medicalRecord);
        if(returnedMedicalRecord != null) {
            logger.info("Medical record updated successfully.");
            return ResponseEntity.ok(returnedMedicalRecord);
        } else {
            logger.error("There was an error when updating the medical record.");
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/medicalRecords/{lastName}/{firstName}")
    public ResponseEntity<Boolean> deleteMedicalRecord(@PathVariable("lastName") String lastName,@PathVariable("firstName") String firstName){
        boolean isMedicalRecordDeleted = medicalRecordService.deleteMedicalRecord(lastName, firstName);
        if (isMedicalRecordDeleted) {
            logger.info("Medical record deleted successfully.");
            return ResponseEntity.ok(isMedicalRecordDeleted);
        } else {
            logger.error("There was an error when deleting the medical record.");
            return ResponseEntity.notFound().build();
        }
    }

}
