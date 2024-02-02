package com.safetynet.safetynetalerts.serviceTests;

import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import com.safetynet.safetynetalerts.service.MedicalRecordService;
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
public class MedicalRecordServiceTests {

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Test
    public void testGetAllFirestations_ShouldReturnAMedicalRecordList() {
        List<MedicalRecord> medicalRecordList = List.of(new MedicalRecord[] {
                new MedicalRecord("John", "Boyd", "03/06/1984", List.of("aznol:350mg", "hydrapermazol:100mg"), List.of("nillacilan")),
                new MedicalRecord("Jacob", "Boyd", "03/06/1989", List.of("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg"), List.of())
        });
        when(medicalRecordRepository.findAll()).thenReturn(medicalRecordList);
        List<MedicalRecord> fetchedMedicalRecordList = medicalRecordService.getAllMedicalRecords();
        assertEquals(2, fetchedMedicalRecordList.size());
    }

    @Test
    public void testGetAllFirestationsWhenEmptyListReturned_ShouldReturnEmptyList() {
        when(medicalRecordRepository.findAll()).thenReturn(new ArrayList<>());
        List<MedicalRecord> fetchedMedicalRecordList = medicalRecordService.getAllMedicalRecords();
        assertEquals(0, fetchedMedicalRecordList.size());
    }

    @Test
    public void testCreateMedicalRecord_ShouldReturnMedicalRecord() {
        MedicalRecord medicalRecord = new MedicalRecord("John", "Boyd", "03/06/1984", List.of("aznol:350mg", "hydrapermazol:100mg"), List.of("nillacilan"));
        when(medicalRecordRepository.save(any(MedicalRecord.class))).thenReturn(medicalRecord);
        MedicalRecord fetchedMedicalRecord = medicalRecordService.createMedicalRecord(medicalRecord);
        assertEquals(fetchedMedicalRecord.getFirstName(), "John");
        assertEquals(fetchedMedicalRecord.getLastName(), "Boyd");
        assertEquals(fetchedMedicalRecord.getBirthdate(), "03/06/1984");
        assertThat(fetchedMedicalRecord.getMedications()).isNotEmpty();
        assertThat(fetchedMedicalRecord.getAllergies()).isNotEmpty();
    }

    @Test
    public void testCreateMedicalRecordWhenErrorWhileSaving_ShouldReturnNull() {
        MedicalRecord medicalRecord = new MedicalRecord("John", "Boyd", "03/06/1984", List.of("aznol:350mg", "hydrapermazol:100mg"), List.of("nillacilan"));
        when(medicalRecordRepository.save(any(MedicalRecord.class))).thenReturn(null);
        MedicalRecord fetchedMedicalRecord = medicalRecordService.createMedicalRecord(medicalRecord);
        assertThat(fetchedMedicalRecord).isNull();
    }

    @Test
    public void testUpdateMedicalRecord_ShouldReturnMedicalRecord() {
        MedicalRecord medicalRecord = new MedicalRecord("John", "Boyd", "03/06/1984", List.of("aznol:350mg", "hydrapermazol:100mg"), List.of("nillacilan"));
        when(medicalRecordRepository.update(any(), any(), any(MedicalRecord.class))).thenReturn(medicalRecord);
        MedicalRecord fetchedMedicalRecord = medicalRecordService.updateMedicalRecord(medicalRecord.getLastName(), medicalRecord.getFirstName(), medicalRecord);
        assertEquals(fetchedMedicalRecord.getFirstName(), "John");
        assertEquals(fetchedMedicalRecord.getLastName(), "Boyd");
        assertEquals(fetchedMedicalRecord.getBirthdate(), "03/06/1984");
        assertThat(fetchedMedicalRecord.getMedications()).isNotEmpty();
        assertThat(fetchedMedicalRecord.getAllergies()).isNotEmpty();
    }

    @Test
    public void testUpdateMedicalRecordWhenErrorWhileUpdating_ShouldReturnNull() {
        MedicalRecord medicalRecord = new MedicalRecord("John", "Boyd", "03/06/1984", List.of("aznol:350mg", "hydrapermazol:100mg"), List.of("nillacilan"));
        when(medicalRecordRepository.update(any(), any(), any(MedicalRecord.class))).thenReturn(null);
        MedicalRecord fetchedMedicalRecord = medicalRecordService.updateMedicalRecord(medicalRecord.getLastName(), medicalRecord.getFirstName(), medicalRecord);
        assertThat(fetchedMedicalRecord).isNull();
    }

    @Test
    public void testDeleteMedicalRecord_ShouldReturnTrue() {
        when(medicalRecordRepository.delete(any(), any())).thenReturn(true);
        boolean isMedicalRecordDeleted = medicalRecordService.deleteMedicalRecord("Boyd", "John");
        assertTrue(isMedicalRecordDeleted);
    }

    @Test
    public void testDeleteMedicalRecordWhenMedicalRecordNotFound_ShouldReturnFalse() {
        when(medicalRecordRepository.delete(any(), any())).thenReturn(false);
        boolean isMedicalRecordDeleted = medicalRecordService.deleteMedicalRecord("Boyd", "John");
        assertFalse(isMedicalRecordDeleted);
    }
}
