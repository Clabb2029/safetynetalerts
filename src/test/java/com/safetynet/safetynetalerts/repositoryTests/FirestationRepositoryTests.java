package com.safetynet.safetynetalerts.repositoryTests;

import com.safetynet.safetynetalerts.model.DataModel;
import com.safetynet.safetynetalerts.model.Firestation;
import com.safetynet.safetynetalerts.repository.FirestationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class FirestationRepositoryTests {

    @InjectMocks
    private static FirestationRepository firestationRepository;

    static DataModel dataModel = new DataModel();

    @BeforeEach
    void setUp() {
        dataModel.getFirestations().clear();
        dataModel.setFirestations(new ArrayList<>(List.of(
                new Firestation[] {
                        new Firestation("1509 Culver St", "3"),
                        new Firestation("29 15th St", "2"),
                        new Firestation("834 Binoc Ave", "3")
                }
        )));
    }

    @Test
    public void testFindAll_ShouldReturnFirestationList() {
        List<Firestation> returnedFirestationList = firestationRepository.findAll();
        assertEquals(returnedFirestationList.size(), 3);
        assertEquals(returnedFirestationList.get(0).getAddress(), "1509 Culver St");
        assertEquals(returnedFirestationList.get(1).getAddress(), "29 15th St");
        assertEquals(returnedFirestationList.get(2).getAddress(), "834 Binoc Ave");
    }

    @Test
    public void testSave_ShouldSaveAndReturnSavedFirestation() {
        Firestation firestation = new Firestation("644 Gershwin Cir", "1");
        firestationRepository.save(firestation);
        assertEquals(dataModel.getFirestations().size(), 4);
        assertEquals(dataModel.getFirestations().get(3).getAddress(), firestation.getAddress());
    }

    @Test
    public void testUpdate_ShouldUpdateAndReturnUpdatedFirestation() {
        Firestation firestation = new Firestation("834 Binoc Ave", "4");
        Firestation returnedFirestation = firestationRepository.updateStation(firestation.getAddress(), firestation);
        assertNotNull(returnedFirestation);
        assertEquals(dataModel.getFirestations().size(), 3);
        assertEquals(dataModel.getFirestations().get(2).getStation(), firestation.getStation());
    }

    @Test
    public void testUpdateWhenAddressNotFound_ShouldReturnEmptyFirestation() {
        Firestation firestation = new Firestation("644 Gershwin Cir", "1");
        Firestation returnedFirestation = firestationRepository.updateStation(firestation.getAddress(), firestation);
        assertNull(returnedFirestation);
    }

    @Test
    public void testDelete_ShouldDeleteAndReturnTrue() {
        boolean isFirestationDeleted = firestationRepository.delete("1509 Culver St", "3");
        assertTrue(isFirestationDeleted);
        assertEquals(dataModel.getFirestations().size(), 2);
    }

    @Test
    public void testDeleteWhenFirestationNotFound_ShouldReturnFalse() {
        boolean isFirestationDeleted = firestationRepository.delete("644 Gershwin Cir", "1");
        assertFalse(isFirestationDeleted);
        assertEquals(dataModel.getFirestations().size(), 3);
    }

    @Test
    public void testFindAllByStationNumber_ShouldReturnAddressList() {
        List<String> returnedFirestationAddressList = firestationRepository.findAllByStationNumber("3");
        assertEquals(returnedFirestationAddressList.size(), 2);
        assertEquals(returnedFirestationAddressList.get(0), "1509 Culver St");
        assertEquals(returnedFirestationAddressList.get(1), "834 Binoc Ave");
    }

    @Test
    public void testFindOneByAddress_ShouldReturnFirestation() {
        Firestation returnedFirestation = firestationRepository.findOneByAddress("1509 Culver St");
        assertThat(returnedFirestation).isNotNull();
        assertEquals(returnedFirestation.getStation(), "3");
    }
}
