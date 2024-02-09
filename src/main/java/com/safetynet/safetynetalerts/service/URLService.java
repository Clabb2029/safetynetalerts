package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.DTO.*;
import com.safetynet.safetynetalerts.model.*;
import com.safetynet.safetynetalerts.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

    public FirestationDTO getPersonsAndCountFromStationNumber(String stationNumber) {
        int adultCount = 0;
        int childrenCount = 0;
        FirestationDTO firestationDTO = new FirestationDTO();

        List<String> addressList = firestationRepository.findAllByStationNumber(stationNumber);

        if (!addressList.isEmpty()) {
            List<FirestationPersonDTO> firestationPersonDTOList = personRepository.findAllByAddressList(addressList);

            if (!firestationPersonDTOList.isEmpty()) {
                firestationDTO.setFirestationPersonDTOList(firestationPersonDTOList);
                List<MedicalRecord> medicalRecordList = medicalRecordRepository.findAllByName(firestationPersonDTOList);

                if (!medicalRecordList.isEmpty()) {
                    for (MedicalRecord medicalRecord : medicalRecordList) {
                        if(getAge(medicalRecord.getBirthdate()) <= 18) {
                            childrenCount++;
                        } else {
                            adultCount++;
                        }
                    }
                    firestationDTO.setAdultCount(adultCount);
                    firestationDTO.setChildrenCount(childrenCount);
                }
            }
        }
        return firestationDTO;
    }

    public ChildAlertDTO getChildrenAndFamilyMembersFromAddress(String address) {
        ChildAlertDTO childAlertDTO = new ChildAlertDTO();
        List<ChildAlertHomeMemberDTO> childrenList = new ArrayList<>();
        List<ChildAlertHomeMemberDTO> adultList = new ArrayList<>();

        List<Person> personList = personRepository.findAllByAddress(address);

        if(!personList.isEmpty()) {
            List<MedicalRecord> medicalRecordList = medicalRecordRepository.findAllByNames(personList);

            if (!medicalRecordList.isEmpty()) {
                for (MedicalRecord medicalRecord : medicalRecordList) {
                    for(Person person : personList) {
                        if(person.getLastName().equals(medicalRecord.getLastName()) && person.getFirstName().equals(medicalRecord.getFirstName())) {
                            int age = getAge(medicalRecord.getBirthdate());
                            ChildAlertHomeMemberDTO childAlertHomeMemberDTO = new ChildAlertHomeMemberDTO(person.getFirstName(), person.getLastName(), age);
                            if (age <= 18) {
                                childrenList.add(childAlertHomeMemberDTO);
                            } else {
                                adultList.add(childAlertHomeMemberDTO);
                            }
                        }
                    }
                }
                childAlertDTO.setChildrenList(childrenList);
                childAlertDTO.setAdultList(adultList);
            }
        }
        return childAlertDTO;
    }

    public List<String> getPhoneListFromFirestationNumber(String firestationNumber){
        List<String> phoneList = new ArrayList<>();
        List<String> addressList = firestationRepository.findAllByStationNumber(firestationNumber);

        if (!addressList.isEmpty()) {
            List<FirestationPersonDTO> firestationPersonDTOList = personRepository.findAllByAddressList(addressList);

            if (!firestationPersonDTOList.isEmpty()) {
                for (FirestationPersonDTO firestationPersonDTO : firestationPersonDTOList) {
                    phoneList.add(firestationPersonDTO.getPhone());
                }
            }
        }
        return phoneList;
    }

    public FireDTO getResidentsMedicalHistoryFromAddress(String address){
        List<Person> personList = personRepository.findAllByAddress(address);
        Firestation firestation = firestationRepository.findOneByAddress(address);
        FireDTO fireDTO = new FireDTO();
        List<FirePersonDTO> personMedicalHistoryDTOList = new ArrayList<>();

        if(!personList.isEmpty() && firestation != null) {
            List<MedicalRecord> medicalRecordList = medicalRecordRepository.findAllByNames(personList);

            if (!medicalRecordList.isEmpty()) {
                for(MedicalRecord medicalRecord : medicalRecordList) {
                    for (Person person : personList) {
                        if(person.getLastName().equals(medicalRecord.getLastName()) && person.getFirstName().equals(medicalRecord.getFirstName())) {
                            int age = getAge(medicalRecord.getBirthdate());
                            FirePersonDTO personMedicalHistoryDTO = new FirePersonDTO(person.getFirstName(), person.getLastName(), person.getPhone(), age, medicalRecord.getMedications(), medicalRecord.getAllergies());
                            personMedicalHistoryDTOList.add(personMedicalHistoryDTO);
                        }
                    }
                    fireDTO.setPersonMedicalHistoryDTOList(personMedicalHistoryDTOList);
                    fireDTO.setStation(firestation.getStation());
                }
            }
        }
        return fireDTO;
    }

    public List<FloodDTO> getHomeListFromFirestationNumbers(List<String> stations) {
        List<FloodDTO> floodDTOList = new ArrayList<>();

        for(String station : stations) {
            FloodDTO floodDTO = new FloodDTO();
            floodDTO.setStation(station);
            List<FloodAddressDTO> floodAddressDTOList = new ArrayList<>();

            List<String> addressList = firestationRepository.findAllByStationNumber(station);

            for(String address : addressList) {
                FloodAddressDTO floodAddressDTO = new FloodAddressDTO();
                floodAddressDTO.setAddress(address);
                List<FirePersonDTO> personMedicalHistoryDTOList = new ArrayList<>();

                List<Person> personList = personRepository.findAllByAddress(address);

                if(!personList.isEmpty()) {
                    List<MedicalRecord> medicalRecordList = medicalRecordRepository.findAllByNames(personList);

                    if(!medicalRecordList.isEmpty()) {
                        for(int i = 0 ; i < personList.size() ; i++) {
                            int finalI = i;
                            MedicalRecord filteredMedicalRecord = medicalRecordList.stream().filter(medicalRecord -> (
                                    medicalRecord.getFirstName().equals(personList.get(finalI).getFirstName())
                                            && medicalRecord.getLastName().equals(personList.get(finalI).getLastName())
                            )).findAny().orElse(null);
                            if(filteredMedicalRecord != null) {
                                int age = getAge(filteredMedicalRecord.getBirthdate());
                                FirePersonDTO firePersonDTO = new FirePersonDTO(personList.get(i).getFirstName(), personList.get(i).getLastName(), personList.get(i).getPhone(), age, filteredMedicalRecord.getMedications(), filteredMedicalRecord.getAllergies());
                                personMedicalHistoryDTOList.add(firePersonDTO);
                                medicalRecordList.remove(filteredMedicalRecord);
                            }
                        }
                        floodAddressDTO.setPersonMedicalHistoryDTOList(personMedicalHistoryDTOList);
                    }
                }
                floodAddressDTOList.add(floodAddressDTO);
            }
            floodDTO.setFloodAddressDTOList(floodAddressDTOList);
            floodDTOList.add(floodDTO);
        }
        return floodDTOList;
    }

    public List<PersonInfoDTO> getInformationAndMedicalHistoryFromFullName(String firstName, String lastName) {
        List<PersonInfoDTO> personInfoDTOList = new ArrayList<>();
        List<Person> personList = personRepository.findAllByFullName(firstName, lastName);
        List<MedicalRecord> medicalRecordList = medicalRecordRepository.findAllByNames(personList);

        if(!medicalRecordList.isEmpty()){
            for (int i = 0 ; i < personList.size() ; i++) {
                int age = getAge(medicalRecordList.get(i).getBirthdate());
                PersonInfoDTO personInfoDTO = new PersonInfoDTO(personList.get(i).getFirstName(), personList.get(i).getLastName(), personList.get(i).getAddress(), age, personList.get(i).getEmail(), medicalRecordList.get(i).getMedications(), medicalRecordList.get(i).getAllergies());
                personInfoDTOList.add(personInfoDTO);
            }
        }
        return personInfoDTOList;
    }

    public List<String> getAllEmailsFromCity(String city) {
        return personRepository.findAllEmailsByCity(city);
    }

}
