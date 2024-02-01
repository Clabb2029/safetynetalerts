package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.service.PersonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    private static final Logger logger = LogManager.getLogger(MedicalRecordController.class);

    @GetMapping("/persons")
    public ResponseEntity<List<Person>> getAllPerson(){
        List<Person> returnedPersonList = personService.getAllPersons();
        if(!returnedPersonList.isEmpty()) {
            logger.info("Person list fetched successfully.");
            return ResponseEntity.ok(returnedPersonList);
        } else {
            logger.error("There was an error when fetching the person list.");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/persons")
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        Person returnedPerson = personService.createPerson(person);
        if(returnedPerson != null) {
            logger.info("Person created successfully.");
            return ResponseEntity.ok(returnedPerson);
        } else {
            logger.error("There was an error when creating the person.");
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/persons/{lastName}/{firstName}")
    public ResponseEntity<Person> updatePerson(@PathVariable("lastName") String lastName,@PathVariable("firstName") String firstName, @RequestBody Person person) {
        Person returnedPerson = personService.updatePerson(lastName, firstName, person);
        if (returnedPerson != null) {
            logger.info("Person updated successfully.");
            return ResponseEntity.ok(returnedPerson);
        } else {
            logger.error("There was an error when updating the person.");
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/persons/{lastName}/{firstName}")
    public ResponseEntity<Boolean> deletePerson(@PathVariable("lastName") String lastName, @PathVariable("firstName") String firstName){
        boolean isPersonDeleted = personService.deletePerson(lastName, firstName);
        if (isPersonDeleted) {
            logger.info("Person deleted successfully.");
            return ResponseEntity.ok(isPersonDeleted);
        } else {
            logger.error("There was an error when deleting the person.");
            return ResponseEntity.notFound().build();
        }
    }

}
