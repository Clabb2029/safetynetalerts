package com.safetynet.safetynetalerts.repository;

import com.safetynet.safetynetalerts.DTO.PersonDTO;
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

    public List<PersonDTO> findAllByAddress(List<String> addressList) {
        List<PersonDTO> personsDTOList = new ArrayList<>();
        dataModel.getPersons().forEach(person -> {
            addressList.forEach(address -> {
                if (person.getAddress().equals(address)){
                    PersonDTO personDTO = new PersonDTO();
                    personDTO.setFirstName(person.getFirstName());
                    personDTO.setLastName(person.getLastName());
                    personDTO.setAddress(person.getAddress());
                    personDTO.setCity(person.getCity());
                    personDTO.setZip(person.getZip());
                    personDTO.setPhone(person.getPhone());
                    personsDTOList.add(personDTO);
                }
            });
        });
        return personsDTOList;
    }

}






