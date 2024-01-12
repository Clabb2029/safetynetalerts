package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.DTO.*;
import com.safetynet.safetynetalerts.model.Firestation;
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

    public LocalDate convertStringDate(String dateToConvert) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return LocalDate.parse(dateToConvert, formatter);
    }

    public int getAge(String birthdate) {
        return Period.between(convertStringDate(birthdate), today).getYears();
    }

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
                        if(getAge(medicalRecord.getBirthdate()) <= 18) {
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

    public HomeMemberDTO getChildrenAndFamilyMembersFromAddress(String address) {
        HomeMemberDTO homeMemberDTO = new HomeMemberDTO();
        List<HomeChildDTO> homeChildDTOList = new ArrayList<>();
        List<HomeOtherMemberDTO> homeOtherMemberDTOList = new ArrayList<>();

        List<Person> personList = personRepository.findAllByAddress(address);

        if(!personList.isEmpty()) {
            List<MedicalRecord> medicalRecordList = medicalRecordRepository.findAllByNames(personList);

            if (!medicalRecordList.isEmpty()) {
                for (MedicalRecord medicalRecord : medicalRecordList) {
                    if(getAge(medicalRecord.getBirthdate()) <= 18) {
                        for (Person person : personList) {
                            if(person.getLastName().equals(medicalRecord.getLastName()) && person.getFirstName().equals(medicalRecord.getFirstName())) {
                                HomeChildDTO homeChildDTO = new HomeChildDTO();
                                int age = getAge(medicalRecord.getBirthdate());
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
                                int age = getAge(medicalRecord.getBirthdate());
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
            }
        }
        return homeMemberDTO;
    }

    public List<String> getPhoneListFromFirestationNumber(String firestationNumber){
        List<String> phoneList = new ArrayList<>();
        List<String> addressList = firestationRepository.findAllByStationNumber(firestationNumber);

        if (!addressList.isEmpty()) {
            List<PersonDTO> personsDTOList = personRepository.findAllByAddressList(addressList);

            if (!personsDTOList.isEmpty()) {
                for (PersonDTO personDTO : personsDTOList) {
                    phoneList.add(personDTO.getPhone());
                }
            }
        }
        return phoneList;
    }

    public PersonMedicalHistoryListDTO getResidentsMedicalHistoryFromAddress(String address){
        List<Person> personList = personRepository.findAllByAddress(address);
        Firestation firestation = firestationRepository.findOneByAddress(address);
        PersonMedicalHistoryListDTO personMedicalHistoryListDTO = new PersonMedicalHistoryListDTO();
        List<PersonMedicalHistoryDTO> personMedicalHistoryDTOList = new ArrayList<>();

        if(!personList.isEmpty() && firestation != null) {
            List<MedicalRecord> medicalRecordList = medicalRecordRepository.findAllByNames(personList);

            if (!medicalRecordList.isEmpty()) {
                for(MedicalRecord medicalRecord : medicalRecordList) {
                    for (Person person : personList) {
                        if(person.getLastName().equals(medicalRecord.getLastName()) && person.getFirstName().equals(medicalRecord.getFirstName())) {
                            PersonMedicalHistoryDTO personMedicalHistoryDTO = new PersonMedicalHistoryDTO();
                            int age = getAge(medicalRecord.getBirthdate());
                            personMedicalHistoryDTO.setFirstName(person.getFirstName());
                            personMedicalHistoryDTO.setLastName(person.getLastName());
                            personMedicalHistoryDTO.setPhone(person.getPhone());
                            personMedicalHistoryDTO.setAge(age);
                            personMedicalHistoryDTO.setMedications(medicalRecord.getMedications());
                            personMedicalHistoryDTO.setAllergies(medicalRecord.getAllergies());
                            personMedicalHistoryDTOList.add(personMedicalHistoryDTO);
                        }
                    }
                    personMedicalHistoryListDTO.setPersonMedicalHistoryDTOList(personMedicalHistoryDTOList);
                    personMedicalHistoryListDTO.setStation(firestation.getStation());
                }
            }
        }
        return personMedicalHistoryListDTO;
    }

    public List<String> getAllEmailsFromCity(String city) {
        return personRepository.findAllEmailByCity(city);
    }

    public List<FloodDTO> getHomeListFromFirestationNumbers(List<String> stations) {
        List<FloodDTO> floodDTOList = new ArrayList<>();
        List<String> addressList = firestationRepository.findAllByStationNumbers(stations);

        if(!addressList.isEmpty()) {
            List<PersonDTO> personDTOList = personRepository.findAllByAddressList(addressList);

            if(!personDTOList.isEmpty()) {
                List<MedicalRecord> medicalRecordList = medicalRecordRepository.findAllByName(personDTOList);

                if(!medicalRecordList.isEmpty()) {
                    for (String station : stations) {
                        FloodDTO floodDTO = new FloodDTO();
                        List<HomeDTO> homeDTOList = new ArrayList<>();
                        floodDTO.setStation(station);
                        for (String address : addressList) {
                            HomeDTO homeDTO = new HomeDTO();
                            List<PersonMedicalHistoryDTO> personMedicalHistoryDTOList = new ArrayList<>();
                            homeDTO.setAddress(address);
                            for(PersonDTO personDTO : personDTOList) {
                                PersonMedicalHistoryDTO personMedicalHistoryDTO = new PersonMedicalHistoryDTO();
                                for(MedicalRecord medicalRecord : medicalRecordList) {
                                    if(personDTO.getFirstName().equals(medicalRecord.getFirstName()) && personDTO.getLastName().equals(medicalRecord.getLastName())) {
                                        int age = getAge(medicalRecord.getBirthdate());
                                        personMedicalHistoryDTO.setFirstName(personDTO.getFirstName());
                                        personMedicalHistoryDTO.setLastName(personDTO.getLastName());
                                        personMedicalHistoryDTO.setPhone(personDTO.getPhone());
                                        personMedicalHistoryDTO.setAge(age);
                                        personMedicalHistoryDTO.setMedications(medicalRecord.getMedications());
                                        personMedicalHistoryDTO.setAllergies(medicalRecord.getAllergies());
                                        personMedicalHistoryDTOList.add(personMedicalHistoryDTO);
                                    }
                                }
                                homeDTO.setPersonMedicalHistoryDTOList(personMedicalHistoryDTOList);
                            }
                            homeDTOList.add(homeDTO);
                        }
                        floodDTO.setHomeDTOList(homeDTOList);
                        floodDTOList.add(floodDTO);
                    }
                }
            }
        }
        return floodDTOList;
    }

}
