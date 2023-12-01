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

    @PutMapping("/persons/{lastName}/{firstName}")
    public Person updatePerson(@PathVariable("lastName") String lastName,@PathVariable("firstName") String firstName, @RequestBody Person person) {
        return personService.updatePerson(lastName, firstName, person);
    }

    @DeleteMapping("/persons/{lastName}/{firstName}")
    public boolean deletePerson(@PathVariable("lastName") String lastName,@PathVariable("firstName") String firstName){
        return personService.deletePerson(lastName, firstName);
    }

}
