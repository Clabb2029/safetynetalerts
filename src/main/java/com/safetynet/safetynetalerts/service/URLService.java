package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.DTO.PersonCountDTO;
import com.safetynet.safetynetalerts.DTO.PersonDTO;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.repository.FirestationRepository;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repository.PersonRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
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


    public PersonCountDTO getPersonsAndCountFromStationNumber(String stationNumber) {
        int adultsCount = 0;
        int childrenCount = 0;
        LocalDate today = LocalDate.now();
        PersonCountDTO personCountDTO = new PersonCountDTO();

        List<String> addressList = firestationRepository.findAllByStationNumber(stationNumber);

        if (!addressList.isEmpty()) {
            List<PersonDTO> personsDTOList = personRepository.findAllByAddress(addressList);

            if (!personsDTOList.isEmpty()) {
                personCountDTO.setPersonDTOList(personsDTOList);
                List<MedicalRecord> medicalRecordsList = medicalRecordRepository.findAllByName(personsDTOList);

                if (!medicalRecordsList.isEmpty()) {
                    for (MedicalRecord medicalRecord : medicalRecordsList) {
                        if(Period.between(convertStringDate(medicalRecord.getBirthdate()), today).getYears() < 18) {
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

}
