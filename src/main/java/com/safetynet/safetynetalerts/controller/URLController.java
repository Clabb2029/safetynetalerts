package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.DTO.*;
import com.safetynet.safetynetalerts.service.URLService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class URLController {

    @Autowired
    private URLService urlService;

    private static final Logger logger = LogManager.getLogger(URLController.class);

    @GetMapping("/firestation")
    public ResponseEntity<FirestationDTO> getPersonsAndCountFromStationNumber(@RequestParam String station_number) {
        FirestationDTO returnedFirestationDTO = urlService.getPersonsAndCountFromStationNumber(station_number);
        if(returnedFirestationDTO.getFirestationPersonDTOList() != null) {
            logger.info("Person list and adults/children count fetched successfully.");
            return ResponseEntity.ok(returnedFirestationDTO);
        } else {
            logger.error("There was an error when fetching the person list and adults/children count.");
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/childAlert")
    public ResponseEntity<ChildAlertDTO> getChildrenAndFamilyMembersFromAddress(@RequestParam String address) {
        ChildAlertDTO returnedChildAlertDTO = urlService.getChildrenAndFamilyMembersFromAddress(address);
        if (returnedChildAlertDTO.getChildrenList() != null && returnedChildAlertDTO.getAdultList() != null) {
            logger.info("Children list and other family members fetched successfully.");
            return ResponseEntity.ok(returnedChildAlertDTO);
        } else {
            logger.error("There was an error when fetching the children list and other family members.");
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/phoneAlert")
    public ResponseEntity<List<String>> getPhoneListFromFirestationNumber(@RequestParam String firestation_number){
        List<String> returnedPhoneList = urlService.getPhoneListFromFirestationNumber(firestation_number);
        if (!returnedPhoneList.isEmpty()){
            logger.info("Phone list from firestation number fetched successfully.");
            return ResponseEntity.ok(returnedPhoneList);
        } else {
            logger.info("There was an error when fetching the phone list from firestation number.");
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/fire")
    public ResponseEntity<FireDTO> getResidentsMedicalHistoryFromAddress(@RequestParam String address) {
        FireDTO returnedFireDTO = urlService.getResidentsMedicalHistoryFromAddress(address);
        if(returnedFireDTO.getPersonMedicalHistoryDTOList() != null || returnedFireDTO.getStation() != null) {
            logger.info("Residents information and medical history fetched successfully.");
            return ResponseEntity.ok(returnedFireDTO);
        } else {
            logger.info("There was an error when fetching the residents information and medical history");
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/flood/stations")
    public ResponseEntity<List<FloodDTO>> getHomeListFromFirestationNumbers(@RequestParam List<String> stations) {
        List<FloodDTO> returnedFloodDTOList = urlService.getHomeListFromFirestationNumbers(stations);
        if(!returnedFloodDTOList.isEmpty()) {
            logger.info("Home list fetched successfully.");
            return ResponseEntity.ok(returnedFloodDTOList);
        } else {
            logger.info("There was an error when fetching the home list.");
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/personInfo")
    public ResponseEntity<List<PersonInfoDTO>> getInformationAndMedicalHistoryFromFullName(@RequestParam String firstName, @RequestParam String lastName) {
        List<PersonInfoDTO> returnedPersonDTOList = urlService.getInformationAndMedicalHistoryFromFullName(firstName, lastName);
        if (!returnedPersonDTOList.isEmpty()) {
            logger.info("Information and medical history fetched successfully.");
            return ResponseEntity.ok(returnedPersonDTOList);
        } else {
            logger.info("There was an error when fetching information and medical history.");
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/communityEmail")
    public ResponseEntity<List<String>> getAllEmailsFromCity(@RequestParam String city) {
        List<String> returnedEmailList = urlService.getAllEmailsFromCity(city);
        if(!returnedEmailList.isEmpty()){
            logger.info("Email list fetched successfully.");
            return ResponseEntity.ok(returnedEmailList);
        } else {
            logger.info("There was an error when fetching the email list.");
            return ResponseEntity.internalServerError().build();
        }
    }

}
