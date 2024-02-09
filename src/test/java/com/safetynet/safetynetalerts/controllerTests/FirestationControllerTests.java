package com.safetynet.safetynetalerts.controllerTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.model.Firestation;
import com.safetynet.safetynetalerts.service.FirestationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class FirestationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private static FirestationService firestationService;

    static List<Firestation> firestationList = new ArrayList<>();

    @BeforeAll
    private static void setUp() {
        Firestation firestation1 = new Firestation("1509 Culver St", "3");
        Firestation firestation2 = new Firestation("29 15th St", "2");
        firestationList.add(firestation1);
        firestationList.add(firestation2);
    }


    //GetFirestations

    @Test
    public void testGetFirestations_ShouldReturnFirestationList() throws Exception {
        when(firestationService.getAllFirestations()).thenReturn(firestationList);
        mockMvc.perform(get("/firestations")).andExpect(status().isOk());
    }

    @Test
    public void testGetFirestationsWhenEmptyList_ShouldReturnNotFound() throws Exception {
        when(firestationService.getAllFirestations()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/firestations")).andExpect(status().isNotFound());
    }


    //CreateFirestation

    @Test
    public void testWhenCreateFirestationWentGood_ShouldReturnFirestation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Firestation firestation = new Firestation();
        firestation.setAddress("1509 Culver St");
        firestation.setStation("5");
        String json = mapper.writeValueAsString(firestation);
        when(firestationService.createFirestation(any(Firestation.class))).thenReturn(firestation);
        mockMvc.perform(post("/firestations")
                .contentType("application/json")
                .content(json))
                .andExpect(status().is(200));
    }

    @Test
    public void testWhenCreateFirestationWentBad_ShouldReturnNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Firestation firestation = new Firestation();
        firestation.setAddress("1509 Culver St");
        firestation.setStation("5");
        String json = mapper.writeValueAsString(firestation);
        when(firestationService.createFirestation(any(Firestation.class))).thenReturn(null);
        mockMvc.perform(post("/firestations")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isBadRequest());
    }


    // UpdateFirestation

    @Test
    public void testUpdateWhenFirestationFound_ShouldReturnFirestation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Firestation firestation = new Firestation();
        firestation.setAddress("1509 Culver St");
        firestation.setStation("5");
        String json = mapper.writeValueAsString(firestation);
        when(firestationService.updateFirestationStation(any(), any(Firestation.class))).thenReturn(firestation);
        mockMvc.perform(put("/firestations/1509 Culver St")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateWhenFirestationNotFound_ShouldReturnNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Firestation firestation = new Firestation();
        firestation.setAddress("1509 Culver St");
        firestation.setStation("5");
        String json = mapper.writeValueAsString(firestation);
        when(firestationService.updateFirestationStation(any(), any(Firestation.class))).thenReturn(null);
        mockMvc.perform(put("/firestations/1509 Culver Street")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isNotFound());
    }


    // deleteFirestation

    @Test
    public void testDeleteWhenFirestationFound_ShouldReturnTrue() throws Exception {
        when(firestationService.deleteFirestation(any(), any())).thenReturn(true);
        mockMvc.perform(delete("/firestations/1509 Culver Street/3"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteWhenFirestationNotFound_ShouldReturnNotFound() throws Exception {
        when(firestationService.deleteFirestation(any(), any())).thenReturn(false);
        mockMvc.perform(delete("/firestations/1509 Culver Street/5"))
                .andExpect(status().isNotFound());
    }

}
