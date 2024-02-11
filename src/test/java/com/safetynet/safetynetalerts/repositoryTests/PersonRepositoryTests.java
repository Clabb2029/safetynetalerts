package com.safetynet.safetynetalerts.repositoryTests;

import com.safetynet.safetynetalerts.DTO.FirestationPersonDTO;
import com.safetynet.safetynetalerts.model.DataModel;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class PersonRepositoryTests {

    @InjectMocks
    private static PersonRepository personRepository;

    static DataModel dataModel = new DataModel();

    @BeforeEach
    void setUp() {
        dataModel.getPersons().clear();
        dataModel.setPersons(new ArrayList<Person>(List.of(
                new Person[] {
                    new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
                    new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com")
                }
        )));
    }

    @Test
    public void testFindAll_ShouldReturnPersonList() {
        List<Person> returnedPersonList = personRepository.findAll();
        assertEquals(returnedPersonList.size(), 2);
        assertEquals(returnedPersonList.get(0).getFirstName(), "John");
        assertEquals(returnedPersonList.get(1).getFirstName(), "Jacob");
    }

    @Test
    public void testSave_ShouldSaveAndReturnSavedPerson() {
        Person person = new Person("Tenley", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "tenz@email.com");
        personRepository.save(person);
        assertEquals(dataModel.getPersons().size(), 3);
        assertEquals(dataModel.getPersons().get(2).getFirstName(), person.getFirstName());
    }

    @Test
    public void testUpdate_ShouldUpdateAndReturnUpdatedPerson() {
        Person person = new Person("Jacob", "Boyd", "1 Culver St", "Culver", "97451", "841-874-1111", "jacob_boyd@email.com");
        Person returnedPerson = personRepository.update(person.getLastName(), person.getFirstName(), person);
        assertNotNull(returnedPerson);
        assertEquals(dataModel.getPersons().size(), 2);
        assertEquals(dataModel.getPersons().get(1).getAddress(), person.getAddress());
        assertEquals(dataModel.getPersons().get(1).getPhone(), person.getPhone());
        assertEquals(dataModel.getPersons().get(1).getEmail(), person.getEmail());
    }

    @Test
    public void testUpdateWhenPersonNotFound_ShouldReturnEmptyPerson() {
        Person person = new Person("Tenley", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "tenz@email.com");
        Person returnedPerson = personRepository.update(person.getLastName(), person.getFirstName(), person);
        assertNull(returnedPerson);
    }

    @Test
    public void testFindByFullName_ShouldReturnPerson() {
        Person returnedPerson = personRepository.findByFullName("Boyd", "Jacob");
        assertNotNull(returnedPerson);
        assertEquals(dataModel.getPersons().get(1), returnedPerson);
    }

    @Test
    public void testDelete_ShouldDeleteAndReturnTrue() {
        boolean isPersonDeleted = personRepository.delete("Boyd", "John");
        assertTrue(isPersonDeleted);
        assertEquals(dataModel.getPersons().size(), 1);
    }

    @Test
    public void testDeleteWhenPersonNotFound_ShouldReturnFalse() {
        boolean isPersonDeleted = personRepository.delete("Boyd", "Tenley");
        assertFalse(isPersonDeleted);
        assertEquals(dataModel.getPersons().size(), 2);
    }

    @Test
    public void testFindAllByAddressList_ShouldReturnAddressList() {
        List<String> addressList = List.of("1509 Culver St");
        List<FirestationPersonDTO> returnedFirestationPersonDTOList = personRepository.findAllByAddressList(addressList);
        assertEquals(returnedFirestationPersonDTOList.size(), 2);
        assertEquals(returnedFirestationPersonDTOList.get(0).getFirstName(), "John");
        assertEquals(returnedFirestationPersonDTOList.get(1).getFirstName(), "Jacob");
    }

    @Test
    public void testFindAllByAddress_ShouldReturnPersonList() {
        String address = "1509 Culver St";
        List<Person> returnedPersonList = personRepository.findAllByAddress(address);
        assertEquals(returnedPersonList.size(), 2);
        assertEquals(returnedPersonList.get(0).getFirstName(), "John");
        assertEquals(returnedPersonList.get(1).getFirstName(), "Jacob");
    }

    @Test
    public void testFindAllEmailsByCity_ShouldReturnEmailList() {
        List<String> returnedEmailList = personRepository.findAllEmailsByCity("Culver");
        assertEquals(returnedEmailList.size(), 2);
        assertEquals(returnedEmailList.get(0), "jaboyd@email.com");
        assertEquals(returnedEmailList.get(1), "drk@email.com");
    }

    @Test
    public void testFindAllByFullName_ShouldReturnPersonList() {
        List<Person> returnedPersonList = personRepository.findAllByFullName("Jacob", "Boyd");
        assertEquals(returnedPersonList.size(), 1);
    }
}
