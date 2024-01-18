package com.safetynet.safetynetalerts.repository;

import com.safetynet.safetynetalerts.DTO.FirestationPersonDTO;
import com.safetynet.safetynetalerts.model.DataModel;
import com.safetynet.safetynetalerts.model.Person;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PersonRepository {

    public static DataModel dataModel = new DataModel();

    public List<Person> findAll() {
        return dataModel.getPersons();
    }

    public Person save(Person person) {
        dataModel.addPerson(person);
        return dataModel.getPersons().stream().filter(f -> (
                f.getFirstName().equals(person.getFirstName())
                && f.getLastName().equals(person.getLastName())
                && f.getAddress().equals(person.getAddress())
                && f.getCity().equals(person.getCity())
                && f.getZip().equals(person.getZip())
                && f.getPhone().equals(person.getPhone())
                && f.getEmail().equals(person.getEmail())
        )).findAny().orElse(null);
    }

    public Person update(String lastName, String firstName, Person person) {
        Person fetchedPerson = findByFullName(lastName, firstName);
        if(!ObjectUtils.isEmpty(fetchedPerson)) {
            fetchedPerson.setAddress(person.getAddress());
            fetchedPerson.setCity(person.getCity());
            fetchedPerson.setZip(person.getZip());
            fetchedPerson.setPhone(person.getPhone());
            fetchedPerson.setEmail(person.getEmail());
        }
        return fetchedPerson;
    }

    public Person findByFullName(String firstName, String lastName) {
        return dataModel.getPersons().stream()
                .filter(person -> (
                        person.getFirstName().equals(firstName) && person.getLastName().equals(lastName))
                ).findAny().orElse(null);
    }

    public boolean delete(String lastName, String firstName) {
        Person fetchedPerson = findByFullName(lastName, firstName);
        if(ObjectUtils.isEmpty(fetchedPerson)) {
            return false;
        } else {
            return dataModel.getPersons().remove(fetchedPerson);
        }
    }

    public List<FirestationPersonDTO> findAllByAddressList(List<String> addressList) {
        List<FirestationPersonDTO> firestationPersonDTOList = new ArrayList<>();
        dataModel.getPersons().forEach(person -> {
            addressList.forEach(address -> {
                if (person.getAddress().equals(address)){
                    FirestationPersonDTO firestationPersonDTO = new FirestationPersonDTO();
                    firestationPersonDTO.setFirstName(person.getFirstName());
                    firestationPersonDTO.setLastName(person.getLastName());
                    firestationPersonDTO.setAddress(person.getAddress());
                    firestationPersonDTO.setPhone(person.getPhone());
                    firestationPersonDTOList.add(firestationPersonDTO);
                }
            });
        });
        return firestationPersonDTOList;
    }

    public List<Person> findAllByAddress(String address) {
        List<Person> personList = new ArrayList<>();
        dataModel.getPersons().forEach(person -> {
            if(person.getAddress().equals(address)) {
                personList.add(person);
            }
        });
        return personList;
    }

    public List<String> findAllEmailsByCity(String city) {
        List<String> emailList = new ArrayList<>();
        dataModel.getPersons().forEach(person -> {
            if(person.getCity().equals(city)) {
                emailList.add(person.getEmail());
            }
        });
        return emailList;
    }

    public List<Person> findAllByFullName(String firstName, String lastName) {
        List<Person> personList = new ArrayList<>();
        dataModel.getPersons().forEach(person -> {
            if(person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
                personList.add(person);
            }
        });
        return personList;
    }

}






