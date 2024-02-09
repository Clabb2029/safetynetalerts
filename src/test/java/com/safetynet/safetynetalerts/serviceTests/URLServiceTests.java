package com.safetynet.safetynetalerts.serviceTests;

import com.safetynet.safetynetalerts.DTO.*;
import com.safetynet.safetynetalerts.model.Firestation;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.repository.FirestationRepository;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repository.PersonRepository;
import com.safetynet.safetynetalerts.service.URLService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class URLServiceTests {

    @InjectMocks
    private URLService urlService;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private FirestationRepository firestationRepository;

    @Test
    public void testConvertStringDate_ShouldReturnConvertedDate() {
        String dateToConvert = "03/06/1984";
        LocalDate convertedDate = urlService.convertStringDate(dateToConvert);
        assertThat(convertedDate).isNotNull();
    }

    @Test
    public void testGetAge_ShouldReturnAge() {
        LocalDate customLocalDate = LocalDate.of(2023, 2, 2);
        try(MockedStatic<LocalDate> guid1 = mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            guid1.when(LocalDate::now).thenReturn(customLocalDate);
            String date = "03/06/1984";
            int age = urlService.getAge(date);
            assertEquals(age, 39);
        };
    }

    @Test
    public void testGetPersonsAndCountFromStationNumber_ShouldReturnFirestationDTO() {
        List<String> addressList = List.of("29 15th St", "892 Downing Ct", "951 LoneTree Rd");
        List<FirestationPersonDTO> firestationPersonDTOList = List.of(
                new FirestationPersonDTO[]{
                        new FirestationPersonDTO("Peter", "Duncan", "644 Gershwin Cir", "841-874-6512"),
                        new FirestationPersonDTO("Reginold", "Walker", "908 73rd St", "841-874-8547"),
                        new FirestationPersonDTO("Jamie", "Peters", "908 73rd St", "841-874-7462"),
                        new FirestationPersonDTO("Brian", "Stelzer", "947 E. Rose Dr", "841-874-7784"),
                        new FirestationPersonDTO("Shawna", "Stelzer", "947 E. Rose Dr", "841-874-7784"),
                        new FirestationPersonDTO("Kendrik", "Stelzer", "947 E. Rose Dr", "841-874-7784"),
                }
        );
        List<MedicalRecord> medicalRecordList = List.of(
                new MedicalRecord[] {
                    new MedicalRecord("Peter", "Duncan", "09/06/2000", List.of(), List.of("shellfish")),
                    new MedicalRecord("Reginold", "Walker", "08/30/1979", List.of("thradox:700mg"), List.of("illisoxian")),
                    new MedicalRecord("Jamie", "Peters", "03/06/1982", List.of(), List.of()),
                    new MedicalRecord("Brian", "Stelzer", "12/06/1975", List.of("ibupurin:200mg", "hydrapermazol:400mg"), List.of("nillacilan")),
                    new MedicalRecord("Shawna", "Stelzer", "07/08/1980", List.of(), List.of()),
                    new MedicalRecord("Kendrik", "Stelzer", "03/06/2014", List.of("noxidian:100mg", "pharmacol:2500mg"), List.of())
                }
        );
        when(firestationRepository.findAllByStationNumber(any())).thenReturn(addressList);
        when(personRepository.findAllByAddressList(any())).thenReturn(firestationPersonDTOList);
        when(medicalRecordRepository.findAllByName(any())).thenReturn(medicalRecordList);

        FirestationDTO returnedfirestationDTO = urlService.getPersonsAndCountFromStationNumber("1");
        verify(firestationRepository, Mockito.times(1)).findAllByStationNumber(any());
        verify(personRepository, Mockito.times(1)).findAllByAddressList(any());
        verify(medicalRecordRepository, Mockito.times(1)).findAllByName(any());
        assertThat(returnedfirestationDTO.getFirestationPersonDTOList()).isNotEmpty();
        assertEquals(returnedfirestationDTO.getAdultCount(), 5);
        assertEquals(returnedfirestationDTO.getChildrenCount(), 1);
    }

    @Test
    public void testGetPersonsAndCountFromStationNumberWhenAddressListIsEmpty_ShouldReturnEmptyFirestationDTO() {
        when(firestationRepository.findAllByStationNumber(any())).thenReturn(new ArrayList<>());

        FirestationDTO returnedfirestationDTO = urlService.getPersonsAndCountFromStationNumber("1");
        assertThat(returnedfirestationDTO.getFirestationPersonDTOList()).isNull();
        verify(personRepository, Mockito.times(0)).findAllByAddressList(any());
        verify(medicalRecordRepository, Mockito.times(0)).findAllByName(any());
    }

    @Test
    public void testGetPersonsAndCountFromStationNumberWhenFirestationPersonDTOListIsEmpty_ShouldReturnEmptyFirestationDTO() {
        List<String> addressList = List.of("29 15th St", "892 Downing Ct", "951 LoneTree Rd");
        when(firestationRepository.findAllByStationNumber(any())).thenReturn(addressList);
        when(personRepository.findAllByAddressList(any())).thenReturn(new ArrayList<>());

        FirestationDTO returnedfirestationDTO = urlService.getPersonsAndCountFromStationNumber("1");
        verify(personRepository, Mockito.times(1)).findAllByAddressList(any());
        verify(medicalRecordRepository, Mockito.times(0)).findAllByName(any());
        assertThat(returnedfirestationDTO.getFirestationPersonDTOList()).isNull();
        assertEquals(returnedfirestationDTO.getAdultCount(), 0);
        assertEquals(returnedfirestationDTO.getChildrenCount(), 0);

    }

    @Test
    public void testGetPersonsAndCountFromStationNumberWhenMedicalRecordListIsEmpty_ShouldReturnUncompletedFirestationDTO() {
        List<String> addressList = List.of("29 15th St", "892 Downing Ct", "951 LoneTree Rd");
        List<FirestationPersonDTO> firestationPersonDTOList = List.of(
                new FirestationPersonDTO[]{
                        new FirestationPersonDTO("Peter", "Duncan", "644 Gershwin Cir", "841-874-6512"),
                        new FirestationPersonDTO("Reginold", "Walker", "908 73rd St", "841-874-8547"),
                        new FirestationPersonDTO("Jamie", "Peters", "908 73rd St", "841-874-7462"),
                        new FirestationPersonDTO("Brian", "Stelzer", "947 E. Rose Dr", "841-874-7784"),
                        new FirestationPersonDTO("Shawna", "Stelzer", "947 E. Rose Dr", "841-874-7784"),
                        new FirestationPersonDTO("Kendrik", "Stelzer", "947 E. Rose Dr", "841-874-7784"),
                }
        );
        when(firestationRepository.findAllByStationNumber(any())).thenReturn(addressList);
        when(personRepository.findAllByAddressList(any())).thenReturn(firestationPersonDTOList);
        when(medicalRecordRepository.findAllByName(any())).thenReturn(new ArrayList<>());

        FirestationDTO returnedfirestationDTO = urlService.getPersonsAndCountFromStationNumber("1");
        verify(personRepository, Mockito.times(1)).findAllByAddressList(any());
        verify(medicalRecordRepository, Mockito.times(1)).findAllByName(any());
        assertEquals(returnedfirestationDTO.getAdultCount(), 0);
        assertEquals(returnedfirestationDTO.getChildrenCount(), 0);
    }

    @Test
    public void testGetChildrenAndFamilyMembersFromAddress_ShouldReturnChildAlertDTO() {
        List<Person> personList = List.of(new Person[]{
                new Person("Peter", "Duncan", "644 Gershwin Cir", "Culver", "97451",  "841-874-6512", "jaboyd@email.com"),
                new Person("Tenley", "Boyd", "644 Gershwin Cir", "Culver", "97451", "841-874-6512", "tenz@email.com")
        });
        List<MedicalRecord> medicalRecordList = List.of(
                new MedicalRecord[] {
                        new MedicalRecord("Peter", "Duncan", "09/06/2000", List.of(), List.of("shellfish")),
                        new MedicalRecord("Tenley", "Boyd", "02/18/2012",  List.of(), List.of("shellfish"))
                }
        );
        when(personRepository.findAllByAddress(any())).thenReturn(personList);
        when(medicalRecordRepository.findAllByNames(any())).thenReturn(medicalRecordList);

        ChildAlertDTO returnedChildAlertDTO = urlService.getChildrenAndFamilyMembersFromAddress("644 Gershwin Cir");
        verify(medicalRecordRepository, Mockito.times(1)).findAllByNames(any());
        assertEquals(returnedChildAlertDTO.getChildrenList().size(), 1);
        assertEquals(returnedChildAlertDTO.getAdultList().size(), 1);
    }

    @Test
    public void testGetChildrenAndFamilyMembersFromAddressWhenPersonListIsEmpty_ShouldReturnEmptyObject() {
        when(personRepository.findAllByAddress(any())).thenReturn(new ArrayList<>());

        ChildAlertDTO returnedChildAlertDTO = urlService.getChildrenAndFamilyMembersFromAddress("644 Gershwin Cir");
        verify(medicalRecordRepository, Mockito.times(0)).findAllByNames(any());
        assertThat(returnedChildAlertDTO.getChildrenList()).isNull();
        assertThat(returnedChildAlertDTO.getAdultList()).isNull();
    }

    @Test
    public void testGetChildrenAndFamilyMembersFromAddressWhenMedicalRecordListIsEmpty_ShouldReturnEmptyObject() {
        List<Person> personList = List.of(new Person[]{
                new Person("Peter", "Duncan", "644 Gershwin Cir", "Culver", "97451",  "841-874-6512", "jaboyd@email.com")
        });
        when(personRepository.findAllByAddress(any())).thenReturn(personList);
        when(medicalRecordRepository.findAllByNames(any())).thenReturn(new ArrayList<>());

        ChildAlertDTO returnedChildAlertDTO = urlService.getChildrenAndFamilyMembersFromAddress("644 Gershwin Cir");
        verify(medicalRecordRepository, Mockito.times(1)).findAllByNames(any());
        assertThat(returnedChildAlertDTO.getChildrenList()).isNull();
        assertThat(returnedChildAlertDTO.getAdultList()).isNull();
    }

    @Test
    public void testGetChildrenAndFamilyMembersFromAddressWhenFirstnamesNotEqual_ShouldReturnEmptyLists() {
        List<Person> personList = List.of(new Person[]{
                new Person("John", "Duncan", "644 Gershwin Cir", "Culver", "97451",  "841-874-6512", "jaboyd@email.com")
        });
        List<MedicalRecord> medicalRecordList = List.of(
                new MedicalRecord[] {
                        new MedicalRecord("Peter", "Duncan", "09/06/2000", List.of(), List.of("shellfish")),
                }
        );
        when(personRepository.findAllByAddress(any())).thenReturn(personList);
        when(medicalRecordRepository.findAllByNames(any())).thenReturn(medicalRecordList);

        ChildAlertDTO returnedChildAlertDTO = urlService.getChildrenAndFamilyMembersFromAddress("644 Gershwin Cir");
        verify(medicalRecordRepository, Mockito.times(1)).findAllByNames(any());
        assertThat(returnedChildAlertDTO.getChildrenList()).isEmpty();
        assertThat(returnedChildAlertDTO.getAdultList()).isEmpty();
    }

    @Test
    public void testGetChildrenAndFamilyMembersFromAddressWhenLastnamesNotEqual_ShouldReturnEmptyObject() {
        List<Person> personList = List.of(new Person[]{
                new Person("Peter", "Boyd", "644 Gershwin Cir", "Culver", "97451",  "841-874-6512", "jaboyd@email.com")
        });
        List<MedicalRecord> medicalRecordList = List.of(
                new MedicalRecord[] {
                        new MedicalRecord("Peter", "Duncan", "09/06/2000", List.of(), List.of("shellfish")),
                }
        );
        when(personRepository.findAllByAddress(any())).thenReturn(personList);
        when(medicalRecordRepository.findAllByNames(any())).thenReturn(medicalRecordList);

        ChildAlertDTO returnedChildAlertDTO = urlService.getChildrenAndFamilyMembersFromAddress("644 Gershwin Cir");
        verify(medicalRecordRepository, Mockito.times(1)).findAllByNames(any());
        assertThat(returnedChildAlertDTO.getChildrenList()).isEmpty();
        assertThat(returnedChildAlertDTO.getAdultList()).isEmpty();
    }

    @Test
    public void testGetResidentsMedicalHistoryFromAddress_ShouldReturnFireDTO() {
        List<Person> personList = List.of(new Person[]{
                new Person("Peter", "Duncan", "644 Gershwin Cir", "Culver", "97451",  "841-874-6512", "jaboyd@email.com")
        });
        Firestation firestation = new Firestation("644 Gershwin Cir", "1");
        List<MedicalRecord> medicalRecordList = List.of(
                new MedicalRecord[] {
                        new MedicalRecord("Peter", "Duncan", "09/06/2000", List.of(), List.of("shellfish")),
                }
        );
        when(personRepository.findAllByAddress(any())).thenReturn(personList);
        when(firestationRepository.findOneByAddress(any())).thenReturn(firestation);
        when(medicalRecordRepository.findAllByNames(any())).thenReturn(medicalRecordList);

        FireDTO returnedFireDTO = urlService.getResidentsMedicalHistoryFromAddress("644 Gershwin Cir");
        verify(medicalRecordRepository, Mockito.times(1)).findAllByNames(any());
        assertEquals(returnedFireDTO.getPersonMedicalHistoryDTOList().size(), 1);
        assertEquals(returnedFireDTO.getStation(), "1");

    }

    @Test
    public void testGetResidentsMedicalHistoryFromAddressWhenFirestationIsNull_ShouldReturnEmptyObject() {
        List<Person> personList = List.of(new Person[]{
                new Person("Peter", "Duncan", "644 Gershwin Cir", "Culver", "97451",  "841-874-6512", "jaboyd@email.com")
        });
        when(personRepository.findAllByAddress(any())).thenReturn(personList);
        when(firestationRepository.findOneByAddress(any())).thenReturn(null);

        FireDTO returnedFireDTO = urlService.getResidentsMedicalHistoryFromAddress("644 Gershwin Cir");
        verify(medicalRecordRepository, Mockito.times(0)).findAllByNames(any());
        assertThat(returnedFireDTO.getPersonMedicalHistoryDTOList()).isNull();
        assertThat(returnedFireDTO.getStation()).isNull();
    }

    @Test
    public void testGetResidentsMedicalHistoryFromAddressWhenPersonListIsEmpty_ShouldReturnEmptyObject() {
        when(personRepository.findAllByAddress(any())).thenReturn(new ArrayList<>());

        FireDTO returnedFireDTO = urlService.getResidentsMedicalHistoryFromAddress("644 Gershwin Cir");
        verify(medicalRecordRepository, Mockito.times(0)).findAllByNames(any());
        assertThat(returnedFireDTO.getPersonMedicalHistoryDTOList()).isNull();
        assertThat(returnedFireDTO.getStation()).isNull();
    }

    @Test
    public void testGetResidentsMedicalHistoryFromAddressWhenMedicalRecordListIsEmpty_ShouldReturnEmptyObject() {
        List<Person> personList = List.of(new Person[]{
                new Person("Peter", "Duncan", "644 Gershwin Cir", "Culver", "97451",  "841-874-6512", "jaboyd@email.com")
        });
        Firestation firestation = new Firestation("644 Gershwin Cir", "1");
        when(personRepository.findAllByAddress(any())).thenReturn(personList);
        when(firestationRepository.findOneByAddress(any())).thenReturn(firestation);
        when(medicalRecordRepository.findAllByNames(any())).thenReturn(new ArrayList<>());

        FireDTO returnedFireDTO = urlService.getResidentsMedicalHistoryFromAddress("644 Gershwin Cir");
        verify(medicalRecordRepository, Mockito.times(1)).findAllByNames(any());
        assertThat(returnedFireDTO.getPersonMedicalHistoryDTOList()).isNull();
        assertThat(returnedFireDTO.getStation()).isNull();
    }

    @Test
    public void testGetResidentsMedicalHistoryFromAddressWhenFirstnamesNotEqual_ShouldReturnEmptyObject() {
        List<Person> personList = List.of(new Person[]{
                new Person("Peter", "Duncan", "644 Gershwin Cir", "Culver", "97451",  "841-874-6512", "jaboyd@email.com")
        });
        Firestation firestation = new Firestation("644 Gershwin Cir", "1");
        List<MedicalRecord> medicalRecordList = List.of(
                new MedicalRecord[] {
                        new MedicalRecord("John", "Duncan", "09/06/2000", List.of(), List.of("shellfish")),
                }
        );
        when(personRepository.findAllByAddress(any())).thenReturn(personList);
        when(firestationRepository.findOneByAddress(any())).thenReturn(firestation);
        when(medicalRecordRepository.findAllByNames(any())).thenReturn(medicalRecordList);

        FireDTO returnedFireDTO = urlService.getResidentsMedicalHistoryFromAddress("644 Gershwin Cir");
        verify(medicalRecordRepository, Mockito.times(1)).findAllByNames(any());
        assertThat(returnedFireDTO.getPersonMedicalHistoryDTOList()).isEmpty();
        assertEquals(returnedFireDTO.getStation(), "1");
    }

    @Test
    public void testGetResidentsMedicalHistoryFromAddressWhenLastnamesNotEqual_ShouldReturnEmptyObject() {
        List<Person> personList = List.of(new Person[]{
                new Person("Peter", "Duncan", "644 Gershwin Cir", "Culver", "97451",  "841-874-6512", "jaboyd@email.com")
        });
        Firestation firestation = new Firestation("644 Gershwin Cir", "1");
        List<MedicalRecord> medicalRecordList = List.of(
                new MedicalRecord[] {
                        new MedicalRecord("Peter", "Boyd", "09/06/2000", List.of(), List.of("shellfish")),
                }
        );
        when(personRepository.findAllByAddress(any())).thenReturn(personList);
        when(firestationRepository.findOneByAddress(any())).thenReturn(firestation);
        when(medicalRecordRepository.findAllByNames(any())).thenReturn(medicalRecordList);

        FireDTO returnedFireDTO = urlService.getResidentsMedicalHistoryFromAddress("644 Gershwin Cir");
        verify(medicalRecordRepository, Mockito.times(1)).findAllByNames(any());
        assertThat(returnedFireDTO.getPersonMedicalHistoryDTOList()).isEmpty();
        assertEquals(returnedFireDTO.getStation(), "1");
    }

    @Test
    public void testGetHomeListFromFirestationNumbers_ShouldReturnFloodDTOList() {
        List<String> addressList = List.of("644 Gershwin Cir");
        List<Person> personList = List.of(
                new Person[]{
                    new Person("Peter", "Duncan", "644 Gershwin Cir", "Culver", "97451",  "841-874-6512", "jaboyd@email.com")
        });
        List<MedicalRecord> medicalRecordList = new ArrayList<MedicalRecord>(List.of(new MedicalRecord("Peter", "Duncan", "09/06/2000", List.of(), List.of("shellfish"))));
        when(firestationRepository.findAllByStationNumber(any())).thenReturn(addressList);
        when(personRepository.findAllByAddress(any())).thenReturn(personList);
        when(medicalRecordRepository.findAllByNames(any())).thenReturn(medicalRecordList);

        List<FloodDTO> returnedFloodDTOList = urlService.getHomeListFromFirestationNumbers(List.of("1"));
        System.out.println(returnedFloodDTOList);
        assertEquals(returnedFloodDTOList.get(0).getStation(), "1");
        assertEquals(returnedFloodDTOList.get(0).getFloodAddressDTOList().get(0).getAddress(), "644 Gershwin Cir");
        assertEquals(returnedFloodDTOList.get(0).getFloodAddressDTOList().get(0).getPersonMedicalHistoryDTOList().size(), 1);
        assertEquals(returnedFloodDTOList.get(0).getFloodAddressDTOList().get(0).getPersonMedicalHistoryDTOList().get(0).getLastName(), "Duncan");
    }

    @Test
    public void testGetHomeListFromFirestationNumbersWhenPersonListIsEmpty_ShouldReturnPersonNull() {
        List<String> addressList = List.of("644 Gershwin Cir");
        when(firestationRepository.findAllByStationNumber(any())).thenReturn(addressList);
        when(personRepository.findAllByAddress(any())).thenReturn(new ArrayList<>());

        List<FloodDTO> returnedFloodDTOList = urlService.getHomeListFromFirestationNumbers(List.of("1"));
        assertThat(returnedFloodDTOList.get(0).getFloodAddressDTOList().get(0).getPersonMedicalHistoryDTOList()).isNull();
    }

    @Test
    public void testGetHomeListFromFirestationNumbersWhenMedicalRecordListIsEmpty_ShouldReturnPersonNull() {
        List<String> addressList = List.of("644 Gershwin Cir");
        List<Person> personList = List.of(new Person[]{
                new Person("Peter", "Duncan", "644 Gershwin Cir", "Culver", "97451",  "841-874-6512", "jaboyd@email.com")
        });
        when(firestationRepository.findAllByStationNumber(any())).thenReturn(addressList);
        when(personRepository.findAllByAddress(any())).thenReturn(personList);
        when(medicalRecordRepository.findAllByNames(any())).thenReturn(new ArrayList<>());

        List<FloodDTO> returnedFloodDTOList = urlService.getHomeListFromFirestationNumbers(List.of("1"));
        assertThat(returnedFloodDTOList.get(0).getFloodAddressDTOList().get(0).getPersonMedicalHistoryDTOList()).isNull();
    }

    @Test
    public void testGetPhoneListFromFirestationNumber_ShouldReturnFirestationNumber() {
        List<String> addressList = List.of("29 15th St", "892 Downing Ct", "951 LoneTree Rd");
        List<FirestationPersonDTO> firestationPersonDTOList = List.of(
                new FirestationPersonDTO[]{
                        new FirestationPersonDTO("Peter", "Duncan", "644 Gershwin Cir", "841-874-6512"),
                        new FirestationPersonDTO("Reginold", "Walker", "908 73rd St", "841-874-8547"),
                        new FirestationPersonDTO("Jamie", "Peters", "908 73rd St", "841-874-7462"),
                        new FirestationPersonDTO("Brian", "Stelzer", "947 E. Rose Dr", "841-874-7784"),
                        new FirestationPersonDTO("Shawna", "Stelzer", "947 E. Rose Dr", "841-874-7784"),
                        new FirestationPersonDTO("Kendrik", "Stelzer", "947 E. Rose Dr", "841-874-7784"),
                }
        );
        when(firestationRepository.findAllByStationNumber(any())).thenReturn(addressList);
        when(personRepository.findAllByAddressList(any())).thenReturn(firestationPersonDTOList);

        List<String> returnedPhoneList = urlService.getPhoneListFromFirestationNumber("1");
        verify(personRepository, Mockito.times(1)).findAllByAddressList(any());
        assertEquals(returnedPhoneList.size(), 6);
    }

    @Test
    public void testGetPhoneListFromFirestationNumberWhenAddressListIsEmpty_ShouldReturnEmptyList() {
        when(firestationRepository.findAllByStationNumber(any())).thenReturn(new ArrayList<>());

        List<String> returnedPhoneList = urlService.getPhoneListFromFirestationNumber("1");
        verify(personRepository, Mockito.times(0)).findAllByAddressList(any());
        assertThat(returnedPhoneList).isEmpty();
    }

    @Test
    public void testGetPhoneListFromFirestationNumberWhenFirestationPersonDTOListIsEmpty_ShouldReturnEmptyList() {
        List<String> addressList = List.of("29 15th St", "892 Downing Ct", "951 LoneTree Rd");
        when(firestationRepository.findAllByStationNumber(any())).thenReturn(addressList);
        when(personRepository.findAllByAddressList(any())).thenReturn(new ArrayList<>());

        List<String> returnedPhoneList = urlService.getPhoneListFromFirestationNumber("1");
        verify(personRepository, Mockito.times(1)).findAllByAddressList(any());
        assertThat(returnedPhoneList).isEmpty();
    }

    @Test
    public void testGetInformationAndMedicalHistoryFromFullName_ShouldReturnListPersonInfoDTO() {
        List<Person> personList = List.of(new Person[]{
                new Person("Peter", "Duncan", "644 Gershwin Cir", "Culver", "97451",  "841-874-6512", "jaboyd@email.com")
        });
        List<MedicalRecord> medicalRecordList = List.of(
                new MedicalRecord[] {
                        new MedicalRecord("Peter", "Duncan", "09/06/2000", List.of(), List.of("shellfish"))
                }
        );
        when(personRepository.findAllByFullName(any(), any())).thenReturn(personList);
        when(medicalRecordRepository.findAllByNames(any())).thenReturn(medicalRecordList);
        List<PersonInfoDTO> returnedPersonInfoDTOList = urlService.getInformationAndMedicalHistoryFromFullName("Peter", "Duncan");
        verify(medicalRecordRepository, Mockito.times(1)).findAllByNames(any());
        assertEquals(returnedPersonInfoDTOList.size(), 1);
    }

    @Test
    public void testGetInformationAndMedicalHistoryFromFullNameWhenPersonListIsEmpty_ShouldReturnEmptyList() {
        when(personRepository.findAllByFullName(any(), any())).thenReturn(new ArrayList<>());
        List<PersonInfoDTO> returnedPersonInfoDTOList = urlService.getInformationAndMedicalHistoryFromFullName("Peter", "Duncan");
        assertThat(returnedPersonInfoDTOList).isEmpty();
    }

    @Test
    public void testGetInformationAndMedicalHistoryFromFullNameWhenMedicalRecordListIsEmpty_ShouldReturnEmptyList() {
        List<Person> personList = List.of(new Person[]{
                new Person("Peter", "Duncan", "644 Gershwin Cir", "Culver", "97451",  "841-874-6512", "jaboyd@email.com")
        });
        when(personRepository.findAllByFullName(any(), any())).thenReturn(personList);
        when(medicalRecordRepository.findAllByNames(any())).thenReturn(new ArrayList<>());
        List<PersonInfoDTO> returnedPersonInfoDTOList = urlService.getInformationAndMedicalHistoryFromFullName("Peter", "Duncan");
        verify(medicalRecordRepository, Mockito.times(1)).findAllByNames(any());
        assertThat(returnedPersonInfoDTOList).isEmpty();
    }

    @Test
    public void testGetAllEmailsFromCity_ShouldReturnAnEmailList() {
        List<String> emailList = List.of("jaboyd@email.com","drk@email.com","tenz@email.com","jaboyd@email.com","jaboyd@email.com","drk@email.com","tenz@email.com","jaboyd@email.com","jaboyd@email.com","tcoop@ymail.com","lily@email.com","soph@email.com","ward@email.com","zarc@email.com","reg@email.com","jpeter@email.com","jpeter@email.com","aly@imail.com","bstel@email.com","ssanw@email.com","bstel@email.com","clivfd@ymail.com","gramps@email.com");
        when(personRepository.findAllEmailsByCity(any())).thenReturn(emailList);
        List<String> fetchedEmailList = urlService.getAllEmailsFromCity("Culver");
        assertEquals(fetchedEmailList.size(), 23);
    }

    @Test
    public void testGetAllEmailsFromCityWhenEmptyListReturned_ShouldReturnEmptyList() {
        when(personRepository.findAllEmailsByCity(any())).thenReturn(new ArrayList<>());
        List<String> fetchedEmailList = urlService.getAllEmailsFromCity("Culver");
        assertEquals(fetchedEmailList.size(), 0);
    }


}
