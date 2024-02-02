package com.safetynet.safetynetalerts.serviceTests;

import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.repository.PersonRepository;
import com.safetynet.safetynetalerts.service.PersonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class PersonServiceTests {

    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonRepository personRepository;

    @Test
    public void testGetAllPersons_ShouldReturnPersonList() {
        List<Person> personList = List.of(new Person[] {
                new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
                new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com"),
        });
        when(personRepository.findAll()).thenReturn(personList);
        List<Person> fetchedPersonList = personService.getAllPersons();
        assertEquals(2, fetchedPersonList.size());
    }

    @Test
    public void testGetAllPersonsWhenEmptyListReturned_ShouldReturnEmptyList() {
        when(personRepository.findAll()).thenReturn(new ArrayList<>());
        List<Person> fetchedPersonList = personService.getAllPersons();
        assertEquals(0, fetchedPersonList.size());
    }

    @Test
    public void testCreatePerson_ShouldReturnPerson() {
        Person person = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        when(personRepository.save(any(Person.class))).thenReturn(person);
        Person fetchedPerson = personService.createPerson(person);
        assertEquals(fetchedPerson.getFirstName(), "John");
        assertEquals(fetchedPerson.getLastName(), "Boyd");
        assertEquals(fetchedPerson.getAddress(), "1509 Culver St");
        assertEquals(fetchedPerson.getCity(), "Culver");
        assertEquals(fetchedPerson.getZip(), "97451");
        assertEquals(fetchedPerson.getPhone(), "841-874-6512");
        assertEquals(fetchedPerson.getEmail(), "jaboyd@email.com");
    }

    @Test
    public void testCreatePersonWhenErrorWhileSaving_ShouldReturnNull() {
        Person person = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        when(personRepository.save(any(Person.class))).thenReturn(null);
        Person fetchedPerson = personService.createPerson(person);
        assertThat(fetchedPerson).isNull();
    }

    @Test
    public void testUpdatePerson_ShouldReturnPerson() {
        Person person = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        when(personRepository.update(any(), any(), any(Person.class))).thenReturn(person);
        Person fetchedPerson = personService.updatePerson(person.getLastName(), person.getFirstName(), person);
        assertEquals(fetchedPerson.getFirstName(), "John");
        assertEquals(fetchedPerson.getLastName(), "Boyd");
        assertEquals(fetchedPerson.getAddress(), "1509 Culver St");
        assertEquals(fetchedPerson.getCity(), "Culver");
        assertEquals(fetchedPerson.getZip(), "97451");
        assertEquals(fetchedPerson.getPhone(), "841-874-6512");
        assertEquals(fetchedPerson.getEmail(), "jaboyd@email.com");
    }

    @Test
    public void testUpdatePersonWhenErrorWhileUpdating_ShouldReturnNull() {
        Person person = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        when(personRepository.update(any(), any(), any(Person.class))).thenReturn(null);
        Person fetchedPerson = personService.updatePerson(person.getLastName(), person.getFirstName(), person);
        assertThat(fetchedPerson).isNull();
    }

    @Test
    public void testDeletePerson_ShouldReturnTrue() {
        when(personRepository.delete(any(), any())).thenReturn(true);
        boolean isPersonDeleted = personService.deletePerson("Boyd", "John");
        assertTrue(isPersonDeleted);
    }

    @Test
    public void testDeletePersonWhenPersonNotFound_ShouldReturnFalse() {
        when(personRepository.delete(any(), any())).thenReturn(false);
        boolean isPersonDeleted = personService.deletePerson("Boyd", "John");
        assertFalse(isPersonDeleted);
    }
}
