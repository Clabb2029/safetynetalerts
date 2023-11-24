package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping("/persons")
    public List<Person> getAllPerson(){
        return personService.getAllPersons();
    }

    @PostMapping("/persons")
    public Person createPerson(@RequestBody Person person) {
        return personService.createPerson(person);
    }

    @PutMapping("/persons/{personFullName}")
    public Person updatePerson(@PathVariable String personFullName, @RequestBody Person person) {
        return personService.updatePerson(personFullName, person);
    }

    @DeleteMapping("/persons/{personFullName}")
    public boolean deletePerson(@PathVariable String personFullName){
        return personService.deletePerson(personFullName);
    }

}
