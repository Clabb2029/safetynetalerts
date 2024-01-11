package com.safetynet.safetynetalerts.controller;


import com.safetynet.safetynetalerts.DTO.PersonCountDTO;
import com.safetynet.safetynetalerts.DTO.HomeMemberDTO;
import com.safetynet.safetynetalerts.service.URLService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
    public PersonCountDTO getPersonsAndCountFromStationNumber(@RequestParam String station_number) {
        PersonCountDTO personCountDTO = urlService.getPersonsAndCountFromStationNumber(station_number);
        if(personCountDTO != null) {
            logger.info("Persons list and adults/children count fetched successfully.");
        } else {
            logger.error("There was an error when fetching the persons list and adults/children count.");
        }
        return personCountDTO;
    }

    @GetMapping("/childAlert")
    public HomeMemberDTO getChildrenAndFamilyMembersFromAddress(@RequestParam String address) {
        HomeMemberDTO homeMemberDTO = urlService.getChildrenAndFamilyMembersFromAddress(address);
        if (homeMemberDTO != null) {
            logger.info("Children list and other family members fetched successfully.");
        } else {
            logger.error("There was an error when fetching the children list and other family members.");
        }
        return homeMemberDTO;
    }

    @GetMapping("/phoneAlert")
    public List<String> getPhoneListFromFirestationNumber(@RequestParam String firestation_number){
        List<String> phoneList = urlService.getPhoneListFromFirestationNumber(firestation_number);
        if (!phoneList.isEmpty()){
            logger.info("Phone list from firestation number fetched successfully.");
        } else {
            logger.info("There was an error when fetching the phone list from firestation number.");
        }
        return phoneList;
    }

}
