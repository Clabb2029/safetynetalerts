package com.safetynet.safetynetalerts.repository;

import com.safetynet.safetynetalerts.model.DataModel;
import com.safetynet.safetynetalerts.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonRepository {

    public List<Person> findAll() {
        return DataModel.persons;
    }

    public Person save(Person person) {
        return null;
    }

    public Person update(String fullName, Person person) {
        return null;
    }

    public boolean delete(String fullName) {
        return true;
    }

}
