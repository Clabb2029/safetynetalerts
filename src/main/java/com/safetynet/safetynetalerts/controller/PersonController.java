package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.service.PersonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    private static final Logger logger = LogManager.getLogger(MedicalRecordController.class);

    @GetMapping("/persons")
    public List<Person> getAllPerson(){
        List<Person> returnedPersonList = personService.getAllPersons();
        if(!returnedPersonList.isEmpty()) {
            logger.info("Person list fetched successfully.");
        } else {
            logger.error("There was an error when fetching the person list.");
        }
        return returnedPersonList;
    }

    @PostMapping("/persons")
    public Person createPerson(@RequestBody Person person) {
        Person returnedPerson = personService.createPerson(person);
        if(returnedPerson != null) {
            logger.info("Person created successfully.");
        } else {
            logger.error("There was an error when creating the person.");
        }
        return returnedPerson;
    }

    @PutMapping("/persons/{lastName}/{firstName}")
    public Person updatePerson(@PathVariable("lastName") String lastName,@PathVariable("firstName") String firstName, @RequestBody Person person) {
        Person returnedPerson = personService.updatePerson(lastName, firstName, person);
        if (returnedPerson != null) {
            logger.info("Person updated successfully.");
        } else {
            logger.error("There was an error when updating the person.");
        }
        return returnedPerson;
    }

    @DeleteMapping("/persons/{lastName}/{firstName}")
    public boolean deletePerson(@PathVariable("lastName") String lastName,@PathVariable("firstName") String firstName){
        boolean isPersonDeleted = personService.deletePerson(lastName, firstName);
        if (isPersonDeleted) {
            logger.info("Person deleted successfully.");
        } else {
            logger.error("There was an error when deleting the person.");
        }
        return isPersonDeleted;
    }

}
