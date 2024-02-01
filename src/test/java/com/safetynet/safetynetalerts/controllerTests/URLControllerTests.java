package com.safetynet.safetynetalerts.controllerTests;

import com.safetynet.safetynetalerts.DTO.*;
import com.safetynet.safetynetalerts.service.URLService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class URLControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private static URLService urlService;

    @Test
    public void testGetFirestation() throws Exception {
        FirestationDTO firestationDTO = new FirestationDTO(
                List.of(
                        new FirestationPersonDTO[]{
                                new FirestationPersonDTO("Peter", "Duncan", "644 Gershwin Cir", "841-874-6512"),
                                new FirestationPersonDTO("Reginold", "Walker", "908 73rd St", "841-874-8547"),
                                new FirestationPersonDTO("Jamie", "Peters", "908 73rd St", "841-874-7462"),
                                new FirestationPersonDTO("Brian", "Stelzer", "947 E. Rose Dr", "841-874-7784"),
                                new FirestationPersonDTO("Shawna", "Stelzer", "947 E. Rose Dr", "841-874-7784"),
                                new FirestationPersonDTO("Kendrik", "Stelzer", "947 E. Rose Dr", "841-874-7784"),
                        }
                ), 5, 1);
        when(urlService.getPersonsAndCountFromStationNumber(any())).thenReturn(firestationDTO);
        mockMvc.perform(get("/firestation?station_number=1")).andExpect(status().isOk());
    }

    @Test
    public void testGetFirestationWhenResponseIsEmpty_ShouldReturnError() throws Exception {
        when(urlService.getPersonsAndCountFromStationNumber(any())).thenReturn(new FirestationDTO());
        mockMvc.perform(get("/firestation?station_number=1")).andExpect(status().isInternalServerError());
    }

    @Test
    public void testGetChildAlert() throws Exception {
        ChildAlertDTO childAlertDTO = new ChildAlertDTO(
                List.of(
                        new ChildAlertHomeMemberDTO("Zach", "Zemicks", 6)
                ),
                List.of(
                        new ChildAlertHomeMemberDTO("Sophia", "Zemicks", 35),
                        new ChildAlertHomeMemberDTO("Warren", "Zemicks", 38)
                )
        );
        when(urlService.getChildrenAndFamilyMembersFromAddress(any())).thenReturn(childAlertDTO);
        mockMvc.perform(get("/childAlert?address=892 Downing Ct")).andExpect(status().isOk());
    }

    @Test
    public void testGetChildAlertWhenResponseIsEmpty_ShouldReturnError() throws Exception {
        when(urlService.getChildrenAndFamilyMembersFromAddress(any())).thenReturn(new ChildAlertDTO());
        mockMvc.perform(get("/phoneAlert?firestation_number=4")).andExpect(status().isInternalServerError());
    }

    @Test
    public void testGetPhoneAlert() throws Exception {
        List<String> phoneList = List.of(new String[]{"841-874-6874", "841-874-9845", "841-874-8888", "841-874-9888"});
        when(urlService.getPhoneListFromFirestationNumber(any())).thenReturn(phoneList);
        mockMvc.perform(get("/phoneAlert?firestation_number=4")).andExpect(status().isOk());
    }

    @Test
    public void testGetPhoneAlertWhenResponseIsEmpty_ShouldReturnError() throws Exception {
        when(urlService.getPhoneListFromFirestationNumber(any())).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/phoneAlert?firestation_number=4")).andExpect(status().isInternalServerError());
    }

    @Test
    public void testGetFire() throws Exception {
        FireDTO fireDTO = new FireDTO(
                List.of(
                        new FirePersonDTO("Sophia", "Zemicks", "841-874-7878", 35, List.of(new String[]{"aznol:60mg", "hydrapermazol:900mg", "pharmacol:5000mg", "terazine:500mg"}), List.of(new String[]{"peanut", "shellfish", "aznol"})),
                        new FirePersonDTO("Warren", "Zemicks", "841-874-7512", 38, List.of(new String[]{}), List.of(new String[]{})),
                        new FirePersonDTO("Zach", "Zemicks", "841-874-7512", 6, List.of(new String[]{}), List.of(new String[]{}))
                ), "2"
        );
        when(urlService.getResidentsMedicalHistoryFromAddress(any())).thenReturn(fireDTO);
        mockMvc.perform(get("/fire?address=892 Downing Ct")).andExpect(status().isOk());
    }

    @Test
    public void testGetFireWhenResponseIsEmpty_ShouldReturnError() throws Exception {
        when(urlService.getResidentsMedicalHistoryFromAddress(any())).thenReturn(new FireDTO());
        mockMvc.perform(get("/fire?address=892 Downing Ct")).andExpect(status().isInternalServerError());
    }

    @Test
    public void testGetFlood() throws Exception {
        List<FloodDTO> floodDTOList = List.of(
                new FloodDTO[]{
                        new FloodDTO("4", List.of(new FloodAddressDTO[]{
                                new FloodAddressDTO("489 Manchester St", List.of(new FirePersonDTO[]{
                                        new FirePersonDTO("Lily", "Cooper", "841-874-9845", 29, List.of(new String[]{}), List.of(new String[]{}))
                                })),
                                new FloodAddressDTO("112 Steppes Pl", List.of(new FirePersonDTO[]{
                                        new FirePersonDTO("Tony", "Cooper", "841-874-6874", 29, List.of(new String[]{"hydrapermazol:300mg", "dodoxadin:30mg"}), List.of(new String[]{"shellfish"})),
                                        new FirePersonDTO("Ron", "Peters", "841-874-8888", 58, List.of(new String[]{}), List.of(new String[]{})),
                                        new FirePersonDTO("Allison", "Boyd", "841-874-9888", 58, List.of(new String[]{"aznol:200mg"}), List.of(new String[]{"nillacilan"}))
                                }))
                        }))
                }
        );
        when(urlService.getHomeListFromFirestationNumbers(any())).thenReturn(floodDTOList);
        mockMvc.perform(get("/flood/stations?stations=4")).andExpect(status().isOk());
    }

    @Test
    public void testGetFloodWhenResponseIsEmpty_ShouldReturnError() throws Exception {
        when(urlService.getHomeListFromFirestationNumbers(any())).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/flood/stations?stations=4")).andExpect(status().isInternalServerError());
    }

    @Test
    public void testGetPersonInfo() throws Exception {
        List<PersonInfoDTO> personInfoDTOList = List.of(new PersonInfoDTO[]{
                new PersonInfoDTO("John", "Boyd", "1509 Culver St", 39, "jaboyd@email.com", List.of(new String[]{"aznol:200mg", "hydrapermazol:100mg"}), List.of(new String[]{"nillacilan"}))
        });
        when(urlService.getInformationAndMedicalHistoryFromFullName(any(), any())).thenReturn(personInfoDTOList);
        mockMvc.perform(get("/personInfo?firstName=John&lastName=Boyd")).andExpect(status().isOk());
    }

    @Test
    public void testGetPersonInfoWhenResponseIsEmpty_ShouldReturnError() throws Exception {
        when(urlService.getInformationAndMedicalHistoryFromFullName(any(), any())).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/personInfo?firstName=John&lastName=Boyd")).andExpect(status().isInternalServerError());
    }

    @Test
    public void testGetCommunityEmail() throws Exception {
        List<String> emailList = List.of("jaboyd@email.com","drk@email.com","tenz@email.com","jaboyd@email.com","jaboyd@email.com","drk@email.com","tenz@email.com","jaboyd@email.com","jaboyd@email.com","tcoop@ymail.com","lily@email.com","soph@email.com","ward@email.com","zarc@email.com","reg@email.com","jpeter@email.com","jpeter@email.com","aly@imail.com","bstel@email.com","ssanw@email.com","bstel@email.com","clivfd@ymail.com","gramps@email.com");
        when(urlService.getAllEmailsFromCity(any())).thenReturn(emailList);
        mockMvc.perform(get("/communityEmail?city=Culver")).andExpect(status().isOk());
    }

    @Test
    public void testGetCommunityEmailWhenResponseIsEmpty_ShouldReturnError() throws Exception {
        when(urlService.getAllEmailsFromCity(any())).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/communityEmail?city=Culver")).andExpect(status().isInternalServerError());
    }

}
