package com.safetynet.safetynetalerts.repositoryTests;

import com.safetynet.safetynetalerts.DTO.FirestationPersonDTO;
import com.safetynet.safetynetalerts.model.DataModel;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
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
public class MedicalRecordRepositoryTests {

    @InjectMocks
    private static MedicalRecordRepository medicalRecordRepository;

    static DataModel dataModel = new DataModel();

    @BeforeEach
    void setUp() {
        dataModel.getMedicalRecords().clear();
        dataModel.setMedicalRecords(new ArrayList<>(List.of(
                new MedicalRecord[]{
                        new MedicalRecord("Peter", "Duncan", "09/06/2000", List.of(), List.of("shellfish")),
                        new MedicalRecord("Reginold", "Walker", "08/30/1979", List.of("thradox:700mg"), List.of("illisoxian")),
                        new MedicalRecord("Jamie", "Peters", "03/06/1982", List.of(), List.of())
                }
        )));
    }

    @Test
    public void testFindAll_ShouldReturnMedicalRecordList() {
        List<MedicalRecord> returnedMedicalRecordList = medicalRecordRepository.findAll();
        assertEquals(returnedMedicalRecordList.size(), 3);
        assertEquals(returnedMedicalRecordList.get(0).getFirstName(), "Peter");
        assertEquals(returnedMedicalRecordList.get(1).getFirstName(), "Reginold");
        assertEquals(returnedMedicalRecordList.get(2).getFirstName(), "Jamie");
    }

    @Test
    public void testSave_ShouldSaveAndReturnSavedMedicalRecord() {
        MedicalRecord medicalRecord = new MedicalRecord("Brian", "Stelzer", "12/06/1975", List.of("ibupurin:200mg", "hydrapermazol:400mg"), List.of("nillacilan"));
        medicalRecordRepository.save(medicalRecord);
        assertEquals(dataModel.getMedicalRecords().size(), 4);
        assertEquals(dataModel.getMedicalRecords().get(3).getFirstName(), "Brian");
    }

    @Test
    public void testUpdate_ShouldUpdateAndReturnUpdatedMedicalRecord() {
        MedicalRecord medicalRecord = new MedicalRecord("Peter", "Duncan", "12/12/2012", List.of("ibupurin:200mg", "hydrapermazol:400mg"), List.of("nillacilan"));
        MedicalRecord returnedMedicalRecord = medicalRecordRepository.update(medicalRecord.getLastName(), medicalRecord.getFirstName(), medicalRecord);
        assertNotNull(returnedMedicalRecord);
        assertEquals(dataModel.getMedicalRecords().size(), 3);
        assertEquals(dataModel.getMedicalRecords().get(0).getBirthdate(), medicalRecord.getBirthdate());
        assertEquals(dataModel.getMedicalRecords().get(0).getMedications(), medicalRecord.getMedications());
        assertEquals(dataModel.getMedicalRecords().get(0).getAllergies(), medicalRecord.getAllergies());
    }

    @Test
    public void testFindByFullName_ShouldReturnMedicalRecord() {
        MedicalRecord returnedMedicalRecord = medicalRecordRepository.findByFullName("Duncan", "Peter");
        assertNotNull(returnedMedicalRecord);
        assertEquals(returnedMedicalRecord, dataModel.getMedicalRecords().get(0));
    }

    @Test
    public void testDelete_ShouldDeleteAndReturnTrue() {
        boolean isMedicalRecordDeleted = medicalRecordRepository.delete("Duncan", "Peter");
        assertTrue(isMedicalRecordDeleted);
        assertEquals(dataModel.getMedicalRecords().size(), 2);
    }

    @Test
    public void testDeleteWhenMedicalRecordNotFound_ShouldReturnFalse() {
        boolean isMedicalRecordDeleted = medicalRecordRepository.delete("Stelzer", "Brian");
        assertFalse(isMedicalRecordDeleted);
        assertEquals(dataModel.getMedicalRecords().size(), 3);
    }

    @Test
    public void testFindAllByName_ShouldReturnMedicalRecordList() {
        List<FirestationPersonDTO> firestationPersonDTOList = List.of(
                new FirestationPersonDTO[]{
                        new FirestationPersonDTO("Peter", "Duncan", "644 Gershwin Cir", "841-874-6512"),
                        new FirestationPersonDTO("Reginold", "Walker", "908 73rd St", "841-874-8547")
                }
        );
        List<MedicalRecord> returnedMedicalRecordList = medicalRecordRepository.findAllByName(firestationPersonDTOList);
        assertEquals(returnedMedicalRecordList.size(), 2);
        assertEquals(returnedMedicalRecordList.get(0).getFirstName(), "Peter");
        assertEquals(returnedMedicalRecordList.get(1).getFirstName(), "Reginold");
    }

    @Test
    public void testFindAllByNames_ShouldReturnMedicalRecordList() {
        List<Person> personList = List.of(new Person[] {
                new Person("Peter", "Duncan", "644 Gershwin Cir", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
                new Person("Reginold", "Walker", "908 73rd St", "Culver", "97451", "841-874-8547", "reg@email.com"),
        });
        List<MedicalRecord> returnedMedicalRecordList = medicalRecordRepository.findAllByNames(personList);
        assertEquals(returnedMedicalRecordList.size(), 2);
        assertEquals(returnedMedicalRecordList.get(0).getFirstName(), "Peter");
        assertEquals(returnedMedicalRecordList.get(1).getFirstName(), "Reginold");
    }
}
