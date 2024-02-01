package com.safetynet.safetynetalerts.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.service.MedicalRecordService;
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
public class MedicalRecordControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicalRecordService medicalRecordService;

    static List<MedicalRecord> medicalRecordList = new ArrayList<>();

    @BeforeAll
    private static void setUp() {
        MedicalRecord medicalRecord1 = new MedicalRecord();
        medicalRecord1.setFirstName("John");
        medicalRecord1.setLastName("Boyd");
        medicalRecord1.setBirthdate("03/06/1984");
        medicalRecord1.setMedications(List.of(new String[]{"aznol:350mg", "hydrapermazol:100mg"}));
        medicalRecord1.setAllergies(List.of(new String[]{"nillacilan"}));

        MedicalRecord medicalRecord2 = new MedicalRecord();
        medicalRecord2.setFirstName("Jacob");
        medicalRecord2.setLastName("Boyd");
        medicalRecord2.setBirthdate("03/06/1989");
        medicalRecord2.setMedications(List.of(new String[]{"pharmacol:5000mg", "terazine:10mg", "noznazol:250mg"}));
        medicalRecord2.setAllergies(List.of(new String[]{}));

        medicalRecordList.add(medicalRecord1);
        medicalRecordList.add(medicalRecord2);
    }


    // Get Medical Records

    @Test
    public void testGetMedicalRecords() throws Exception {
        when(medicalRecordService.getAllMedicalRecords()).thenReturn(medicalRecordList);
        mockMvc.perform(get("/medicalRecords")).andExpect(status().isOk());
    }

    @Test
    public void testGetFirestationsWhenEmptyList() throws Exception {
        List<MedicalRecord> medicalRecordListCopy = (List<MedicalRecord>)((ArrayList<MedicalRecord>)medicalRecordList).clone();
        medicalRecordListCopy.clear();
        when(medicalRecordService.getAllMedicalRecords()).thenReturn(medicalRecordListCopy);
        mockMvc.perform(get("/medicalRecords")).andExpect(status().isNotFound());
    }


    // Create MedicalnRecord

    @Test
    public void testWhenCreateMedicalRecordWentGood_ShouldReturnMedicalRecord() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("Johanna");
        medicalRecord.setLastName("Bold");
        medicalRecord.setBirthdate("03/06/1980");
        medicalRecord.setMedications(List.of(new String[]{"hydrapermazol:100mg"}));
        medicalRecord.setAllergies(List.of(new String[]{"nillacilan"}));
        String json = mapper.writeValueAsString(medicalRecord);
        when(medicalRecordService.createMedicalRecord(any(MedicalRecord.class))).thenReturn(medicalRecord);
        mockMvc.perform(post("/medicalRecords")
                .contentType("application/json")
                .content(json))
                .andExpect(status().is(200));
    }

    @Test
    public void testWhenCreateMedicalRecordWentBad_ShouldReturnMedicalRecord() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("Johanna");
        medicalRecord.setLastName("Bold");
        medicalRecord.setBirthdate("03/06/1980");
        medicalRecord.setMedications(List.of(new String[]{"hydrapermazol:100mg"}));
        medicalRecord.setAllergies(List.of(new String[]{"nillacilan"}));
        String json = mapper.writeValueAsString(medicalRecord);
        when(medicalRecordService.createMedicalRecord(any(MedicalRecord.class))).thenReturn(null);
        mockMvc.perform(post("/medicalRecords")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isBadRequest());
    }


    // Update Medical Record

    @Test
    public void testUpdateWhenMedicalRecordFound_ShouldReturnMedicalRecord() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Boyd");
        medicalRecord.setBirthdate("03/06/1980");
        medicalRecord.setMedications(List.of(new String[]{"tradoxidine:400mg"}));
        medicalRecord.setAllergies(List.of(new String[]{"peanut", "shellfish", "aznol"}));
        String json = mapper.writeValueAsString(medicalRecord);
        when(medicalRecordService.updateMedicalRecord(any(), any(), any(MedicalRecord.class))).thenReturn(medicalRecord);
        mockMvc.perform(put("/medicalRecords/Boyd/John")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateWhenMedicalRecordFound_ShouldReturnNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Boyd");
        medicalRecord.setBirthdate("03/06/1980");
        medicalRecord.setMedications(List.of(new String[]{"tradoxidine:400mg"}));
        medicalRecord.setAllergies(List.of(new String[]{"peanut", "shellfish", "aznol"}));
        String json = mapper.writeValueAsString(medicalRecord);
        when(medicalRecordService.updateMedicalRecord(any(), any(), any(MedicalRecord.class))).thenReturn(null);
        mockMvc.perform(put("/medicalRecords/Boyd/John")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isNotFound());
    }


    // Delete Medical Record

    @Test
    public void testDeleteWhenMedicalRecordFound_ShouldReturnTrue() throws Exception {
        when(medicalRecordService.deleteMedicalRecord(any(), any())).thenReturn(true);
        mockMvc.perform(delete("/medicalRecords/Boyd/John"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteWhenMedicalRecordNotFound_ShouldReturnFalse() throws Exception {
        when(medicalRecordService.deleteMedicalRecord(any(), any())).thenReturn(false);
        mockMvc.perform(delete("/medicalRecords/Boyd/John"))
                .andExpect(status().isNotFound());
    }

}
