package com.safetynet.safetynetalerts.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.service.PersonService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class PersonControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private static PersonService personService;

    static List<Person> personList = new ArrayList<>();

    @BeforeAll
    private static void setUp() {
        Person person1 = new Person();
        person1.setFirstName("John");
        person1.setLastName("Boyd");
        person1.setAddress("1509 Culver St");
        person1.setCity("Culver");
        person1.setZip("97451");
        person1.setPhone("841-874-6512");
        person1.setEmail("jaboyd@email.com");

        Person person2 = new Person();
        person2.setFirstName("Jacob");
        person2.setLastName("Boyd");
        person2.setAddress("1509 Culver St");
        person2.setCity("Culver");
        person2.setZip("97451");
        person2.setPhone("841-874-6513");
        person2.setEmail("drk@email.com");

        personList.add(person1);
        personList.add(person2);
    }


    // Get Persons

    @Test
    public void testGetPersons() throws Exception {
        when(personService.getAllPersons()).thenReturn(personList);
        mockMvc.perform(get("/persons")).andExpect(status().isOk());
    }

    @Test
    public void testGetPersonsWhenEmptyList() throws Exception {
        List<Person> personListCopy = (List<Person>)((ArrayList<Person>)personList).clone();
        personListCopy.clear();
        when(personService.getAllPersons()).thenReturn(personListCopy);
        mockMvc.perform(get("/persons")).andExpect(status().isNotFound());
    }


    // Create Person

    @Test
    public void testWhenCreatePersonWentGood_ShouldReturnPerson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Person person = new Person();
        person.setFirstName("Tenley");
        person.setLastName("Boyd");
        person.setAddress("1509 Culver St");
        person.setCity("Culver");
        person.setZip("97451");
        person.setPhone("841-874-6512");
        person.setEmail("tenz@email.com");
        String json = mapper.writeValueAsString(person);
        when(personService.createPerson(any(Person.class))).thenReturn(person);
        mockMvc.perform(post("/persons")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().is(200));
    }

    @Test
    public void testWhenCreatePersonWentBad_ShouldReturnNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Person person = new Person();
        person.setFirstName("Tenley");
        person.setLastName("Boyd");
        person.setAddress("1509 Culver St");
        person.setCity("Culver");
        person.setZip("97451");
        person.setPhone("841-874-6512");
        person.setEmail("tenz@email.com");
        String json = mapper.writeValueAsString(person);
        when(personService.createPerson(any(Person.class))).thenReturn(null);
        mockMvc.perform(post("/persons")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }


    // Create Person

    @Test
    public void testUpdateWhenPersonFound_ShouldReturnPerson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Person person = new Person();
        person.setFirstName("Tenley");
        person.setLastName("Boyd");
        person.setAddress("1509 Culver St");
        person.setCity("Culver");
        person.setZip("97451");
        person.setPhone("841-874-6512");
        person.setEmail("tenz@email.com");
        String json = mapper.writeValueAsString(person);
        when(personService.updatePerson(any(), any(), any(Person.class))).thenReturn(person);
        mockMvc.perform(put("/persons/Boyd/Tenley")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateWhenPersonNotFound_ShouldReturnNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Person person = new Person();
        person.setFirstName("Tenley");
        person.setLastName("Boyd");
        person.setAddress("1509 Culver St");
        person.setCity("Culver");
        person.setZip("97451");
        person.setPhone("841-874-6512");
        person.setEmail("tenz@email.com");
        String json = mapper.writeValueAsString(person);
        when(personService.updatePerson(any(), any(), any(Person.class))).thenReturn(null);
        mockMvc.perform(put("/persons/Boyd/Tenley")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isNotFound());
    }


    // Delete Person

    @Test
    public void testDeleteWhenPersonFound_ShouldReturnTrue() throws Exception {
        when(personService.deletePerson(any(), any())).thenReturn(true);
        mockMvc.perform(delete("/persons/Boyd/John"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteWhenPersonNotFound_ShouldReturnFalse() throws Exception {
        when(personService.deletePerson(any(), any())).thenReturn(false);
        mockMvc.perform(delete("/persons/Boyd/John"))
                .andExpect(status().isNotFound());
    }

}
