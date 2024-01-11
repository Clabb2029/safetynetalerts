package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.DTO.*;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.repository.FirestationRepository;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repository.PersonRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class URLService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private FirestationRepository firestationRepository;

    LocalDate today = LocalDate.now();

    public PersonCountDTO getPersonsAndCountFromStationNumber(String stationNumber) {
        int adultsCount = 0;
        int childrenCount = 0;
        PersonCountDTO personCountDTO = new PersonCountDTO();

        List<String> addressList = firestationRepository.findAllByStationNumber(stationNumber);

        if (!addressList.isEmpty()) {
            List<PersonDTO> personsDTOList = personRepository.findAllByAddressList(addressList);

            if (!personsDTOList.isEmpty()) {
                personCountDTO.setPersonDTOList(personsDTOList);
                List<MedicalRecord> medicalRecordsList = medicalRecordRepository.findAllByName(personsDTOList);

                if (!medicalRecordsList.isEmpty()) {
                    for (MedicalRecord medicalRecord : medicalRecordsList) {
                        if(Period.between(convertStringDate(medicalRecord.getBirthdate()), today).getYears() <= 18) {
                            childrenCount++;
                        } else {
                            adultsCount++;
                        }
                    }
                    personCountDTO.setAdultCount(adultsCount);
                    personCountDTO.setChildrenCount(childrenCount);
                } else {
                    personCountDTO = null;
                }
            } else {
                personCountDTO = null;
            }
        } else {
            personCountDTO = null;
        }
        return personCountDTO;
    }

    public LocalDate convertStringDate(String dateToConvert) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return LocalDate.parse(dateToConvert, formatter);
    }

    public HomeMemberDTO getChildrenAndFamilyMembersFromAddress(String address) {
        HomeMemberDTO homeMemberDTO = new HomeMemberDTO();
        List<HomeChildDTO> homeChildDTOList = new ArrayList<>();
        List<HomeOtherMemberDTO> homeOtherMemberDTOList = new ArrayList<>();

        List<Person> personList = personRepository.findAllByAddress(address);

        if(!personList.isEmpty()) {
            List<MedicalRecord> medicalRecordList = medicalRecordRepository.findAllByNames(personList);

            if (!medicalRecordList.isEmpty()) {
                for (MedicalRecord medicalRecord : medicalRecordList) {
                    if(Period.between(convertStringDate(medicalRecord.getBirthdate()), today).getYears() <= 18) {
                        for (Person person : personList) {
                            if(person.getLastName().equals(medicalRecord.getLastName()) && person.getFirstName().equals(medicalRecord.getFirstName())) {
                                HomeChildDTO homeChildDTO = new HomeChildDTO();
                                int age = Period.between(convertStringDate(medicalRecord.getBirthdate()), today).getYears();
                                homeChildDTO.setFirstName(person.getFirstName());
                                homeChildDTO.setLastName(person.getLastName());
                                homeChildDTO.setAge(age);
                                homeChildDTOList.add(homeChildDTO);
                            }
                        }
                    } else {
                        for (Person person : personList) {
                            if(person.getLastName().equals(medicalRecord.getLastName()) && person.getFirstName().equals(medicalRecord.getFirstName())) {
                                HomeOtherMemberDTO homeOtherMemberDTO = new HomeOtherMemberDTO();
                                int age = Period.between(convertStringDate(medicalRecord.getBirthdate()), today).getYears();
                                homeOtherMemberDTO.setFirstName(person.getFirstName());
                                homeOtherMemberDTO.setLastName(person.getLastName());
                                homeOtherMemberDTO.setAge(age);
                                homeOtherMemberDTOList.add(homeOtherMemberDTO);
                            }
                        }
                    }
                }
                homeMemberDTO.setHomeChildDTOList(homeChildDTOList);
                homeMemberDTO.setHomeOtherMemberDTOList(homeOtherMemberDTOList);
            } else {
                homeMemberDTO = null;
            }
        } else {
            homeMemberDTO = null;
        }
        return homeMemberDTO;
    }

}
