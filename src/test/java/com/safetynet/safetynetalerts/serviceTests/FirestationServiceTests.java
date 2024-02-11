package com.safetynet.safetynetalerts.serviceTests;

import com.safetynet.safetynetalerts.model.Firestation;
import com.safetynet.safetynetalerts.repository.FirestationRepository;
import com.safetynet.safetynetalerts.service.FirestationService;
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
public class FirestationServiceTests {

    @InjectMocks
    private static FirestationService firestationService;

    @Mock
    private static FirestationRepository firestationRepository;


    // tests getAllFirestations

    @Test
    public void testGetAllFirestations_ShouldReturnAFirestationList() {
        List<Firestation> firestationList = List.of(new Firestation[]{
                new Firestation("1509 Culver St", "3"),
                new Firestation("29 15th St", "2")
        });
        when(firestationRepository.findAll()).thenReturn(firestationList);
        List<Firestation> fetchedFirestationList = firestationService.getAllFirestations();
        assertEquals(2, fetchedFirestationList.size());
    }

    @Test
    public void testGetAllFirestationsWhenEmptyListReturned_ShouldReturnEmptyList() {
        when(firestationRepository.findAll()).thenReturn(new ArrayList<>());
        List<Firestation> fetchedFirestationList = firestationService.getAllFirestations();
        assertEquals(0, fetchedFirestationList.size());
    }


    // tests createFirestation

    @Test
    public void testCreateFirestation_ShouldReturnFirestation() {
        Firestation firestation = new Firestation("834 Binoc Ave", "3");
        when(firestationRepository.save(any(Firestation.class))).thenReturn(firestation);
        Firestation fetchedFirestation = firestationService.createFirestation(firestation);
        assertEquals(fetchedFirestation.getAddress(), "834 Binoc Ave");
        assertEquals(fetchedFirestation.getStation(), "3");
    }

    @Test
    public void testCreateFirestationWhenErrorWhileSaving_ShouldReturnNull() {
        Firestation firestation = new Firestation("834 Binoc Ave", "3");
        when(firestationRepository.save(any(Firestation.class))).thenReturn(null);
        Firestation fetchedFirestation = firestationService.createFirestation(firestation);
        assertThat(fetchedFirestation).isNull();
    }


    // tests updateFirestationStation

    @Test
    public void testUpdateFirestationStation_ShouldReturnFirestation() {
        Firestation firestation = new Firestation("1509 Culver St", "4");
        when(firestationRepository.updateStation(any(), any(Firestation.class))).thenReturn(firestation);
        Firestation fetchedFirestation = firestationService.updateFirestationStation(firestation.getAddress(), firestation);
        assertEquals(fetchedFirestation.getAddress(), "1509 Culver St");
        assertEquals(fetchedFirestation.getStation(), "4");
    }

    @Test
    public void testUpdateFirestationStationWhenErrorWhileUpdating_ShouldReturnNull() {
        Firestation firestation = new Firestation("1509 Culver St", "4");
        when(firestationRepository.updateStation(any(), any(Firestation.class))).thenReturn(null);
        Firestation fetchedFirestation = firestationService.updateFirestationStation(firestation.getAddress(), firestation);
        assertThat(fetchedFirestation).isNull();
    }


    // tests deleteFirestation

    @Test
    public void testDeleteFirestation_ShouldReturnTrue() {
        when(firestationRepository.delete(any(), any())).thenReturn(true);
        boolean isFirestationDeleted = firestationService.deleteFirestation("1509 Culver St", "3");
        assertTrue(isFirestationDeleted);
    }

    @Test
    public void testDeleteFirestationWhenFirestationNotFound_ShouldReturnFalse() {
        when(firestationRepository.delete(any(), any())).thenReturn(false);
        boolean isFirestationDeleted = firestationService.deleteFirestation("1509 Culver St", "3");
        assertFalse(isFirestationDeleted);
    }
}
