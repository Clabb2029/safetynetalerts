package com.safetynet.safetynetalerts.serviceTests;

import com.safetynet.safetynetalerts.DTO.*;
import com.safetynet.safetynetalerts.model.Firestation;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.repository.FirestationRepository;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repository.PersonRepository;
import com.safetynet.safetynetalerts.service.URLService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    static String dateToConvert = "03/06/1984";

    List<FirestationPersonDTO> firestationPersonDTOList1 = List.of(
            new FirestationPersonDTO[]{
                    new FirestationPersonDTO("Peter", "Duncan", "644 Gershwin Cir", "841-874-6512"),
                    new FirestationPersonDTO("Reginold", "Walker", "908 73rd St", "841-874-8547"),
                    new FirestationPersonDTO("Jamie", "Peters", "908 73rd St", "841-874-7462"),
                    new FirestationPersonDTO("Brian", "Stelzer", "947 E. Rose Dr", "841-874-7784"),
                    new FirestationPersonDTO("Shawna", "Stelzer", "947 E. Rose Dr", "841-874-7784"),
                    new FirestationPersonDTO("Kendrik", "Stelzer", "947 E. Rose Dr", "841-874-7784"),
            }
    );

    List<MedicalRecord> medicalRecordList1 = List.of(
            new MedicalRecord[] {
                    new MedicalRecord("Peter", "Duncan", "09/06/2000", List.of(), List.of("shellfish")),
                    new MedicalRecord("Reginold", "Walker", "08/30/1979", List.of("thradox:700mg"), List.of("illisoxian")),
                    new MedicalRecord("Jamie", "Peters", "03/06/1982", List.of(), List.of()),
                    new MedicalRecord("Brian", "Stelzer", "12/06/1975", List.of("ibupurin:200mg", "hydrapermazol:400mg"), List.of("nillacilan")),
                    new MedicalRecord("Shawna", "Stelzer", "07/08/1980", List.of(), List.of()),
                    new MedicalRecord("Kendrik", "Stelzer", "03/06/2014", List.of("noxidian:100mg", "pharmacol:2500mg"), List.of())
            }
    );

    List<MedicalRecord> medicalRecordList2 = List.of(
            new MedicalRecord[] {
                    new MedicalRecord("Peter", "Duncan", "09/06/2000", List.of(), List.of("shellfish")),
            }
    );

    List<String> addressList1 = List.of("29 15th St", "892 Downing Ct", "951 LoneTree Rd");

    List<String> addressList2 = List.of("644 Gershwin Cir");

    List<Person> personList1 = List.of(new Person[]{
            new Person("Peter", "Duncan", "644 Gershwin Cir", "Culver", "97451",  "841-874-6512", "jaboyd@email.com")
    });

    Firestation firestation1 = new Firestation("644 Gershwin Cir", "1");


    // test convertStringDate

    @Test
    public void testConvertStringDate_ShouldReturnConvertedDate() {
        LocalDate convertedDate = urlService.convertStringDate(dateToConvert);
        assertThat(convertedDate).isNotNull();
    }


    // test getAge

    @Test
    public void testGetAge_ShouldReturnAge() {
        LocalDate customLocalDate = LocalDate.of(2023, 2, 2);
        try(MockedStatic<LocalDate> guid1 = mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            guid1.when(LocalDate::now).thenReturn(customLocalDate);
            int age = urlService.getAge(dateToConvert);
            assertEquals(age, 39);
        };
    }


    // tests getPersonsAndCountFromStationNumber

    @Test
    public void testGetPersonsAndCountFromStationNumber_ShouldReturnFirestationDTO() {
        when(firestationRepository.findAllByStationNumber(any())).thenReturn(addressList1);
        when(personRepository.findAllByAddressList(any())).thenReturn(firestationPersonDTOList1);
        when(medicalRecordRepository.findAllByName(any())).thenReturn(medicalRecordList1);

        FirestationDTO returnedFirestationDTO = urlService.getPersonsAndCountFromStationNumber("1");
        verify(firestationRepository, Mockito.times(1)).findAllByStationNumber(any());
        verify(personRepository, Mockito.times(1)).findAllByAddressList(any());
        verify(medicalRecordRepository, Mockito.times(1)).findAllByName(any());
        assertThat(returnedFirestationDTO.getFirestationPersonDTOList()).isNotEmpty();
        assertEquals(returnedFirestationDTO.getAdultCount(), 5);
        assertEquals(returnedFirestationDTO.getChildrenCount(), 1);
    }

    @Test
    public void testGetPersonsAndCountFromStationNumberWhenAddressListIsEmpty_ShouldReturnNull() {
        when(firestationRepository.findAllByStationNumber(any())).thenReturn(new ArrayList<>());

        FirestationDTO returnedFirestationDTO = urlService.getPersonsAndCountFromStationNumber("1");
        assertThat(returnedFirestationDTO).isNull();
        verify(personRepository, Mockito.times(0)).findAllByAddressList(any());
        verify(medicalRecordRepository, Mockito.times(0)).findAllByName(any());
    }

    @Test
    public void testGetPersonsAndCountFromStationNumberWhenFirestationPersonDTOListIsEmpty_ShouldReturnNull() {
        when(firestationRepository.findAllByStationNumber(any())).thenReturn(addressList1);
        when(personRepository.findAllByAddressList(any())).thenReturn(new ArrayList<>());

        FirestationDTO returnedFirestationDTO = urlService.getPersonsAndCountFromStationNumber("1");
        verify(personRepository, Mockito.times(1)).findAllByAddressList(any());
        verify(medicalRecordRepository, Mockito.times(0)).findAllByName(any());
        assertThat(returnedFirestationDTO).isNull();
    }

    @Test
    public void testGetPersonsAndCountFromStationNumberWhenMedicalRecordListIsEmpty_ShouldReturnNull() {
        when(firestationRepository.findAllByStationNumber(any())).thenReturn(addressList1);
        when(personRepository.findAllByAddressList(any())).thenReturn(firestationPersonDTOList1);
        when(medicalRecordRepository.findAllByName(any())).thenReturn(new ArrayList<>());

        FirestationDTO returnedFirestationDTO = urlService.getPersonsAndCountFromStationNumber("1");
        verify(personRepository, Mockito.times(1)).findAllByAddressList(any());
        verify(medicalRecordRepository, Mockito.times(1)).findAllByName(any());
        assertThat(returnedFirestationDTO).isNull();
    }


    // tests getChildrenAndFamilyMembersFromAddress

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
    public void testGetChildrenAndFamilyMembersFromAddressWhenPersonListIsEmpty_ShouldReturnNull() {
        when(personRepository.findAllByAddress(any())).thenReturn(new ArrayList<>());

        ChildAlertDTO returnedChildAlertDTO = urlService.getChildrenAndFamilyMembersFromAddress("644 Gershwin Cir");
        verify(medicalRecordRepository, Mockito.times(0)).findAllByNames(any());
        assertThat(returnedChildAlertDTO).isNull();
    }

    @Test
    public void testGetChildrenAndFamilyMembersFromAddressWhenMedicalRecordListIsEmpty_ShouldReturnNull() {
        when(personRepository.findAllByAddress(any())).thenReturn(personList1);
        when(medicalRecordRepository.findAllByNames(any())).thenReturn(new ArrayList<>());

        ChildAlertDTO returnedChildAlertDTO = urlService.getChildrenAndFamilyMembersFromAddress("644 Gershwin Cir");
        verify(medicalRecordRepository, Mockito.times(1)).findAllByNames(any());
        assertThat(returnedChildAlertDTO).isNull();
    }


    // test getResidentsMedicalHistoryFromAddress

    @Test
    public void testGetResidentsMedicalHistoryFromAddress_ShouldReturnFireDTO() {
        when(personRepository.findAllByAddress(any())).thenReturn(personList1);
        when(firestationRepository.findOneByAddress(any())).thenReturn(firestation1);
        when(medicalRecordRepository.findAllByNames(any())).thenReturn(medicalRecordList2);

        FireDTO returnedFireDTO = urlService.getResidentsMedicalHistoryFromAddress("644 Gershwin Cir");
        verify(medicalRecordRepository, Mockito.times(1)).findAllByNames(any());
        assertEquals(returnedFireDTO.getPersonMedicalHistoryDTOList().size(), 1);
        assertEquals(returnedFireDTO.getStation(), "1");

    }

    @Test
    public void testGetResidentsMedicalHistoryFromAddressWhenFirestationIsNull_ShouldReturnNull() {
        when(personRepository.findAllByAddress(any())).thenReturn(personList1);
        when(firestationRepository.findOneByAddress(any())).thenReturn(null);

        FireDTO returnedFireDTO = urlService.getResidentsMedicalHistoryFromAddress("644 Gershwin Cir");
        verify(medicalRecordRepository, Mockito.times(0)).findAllByNames(any());
        assertThat(returnedFireDTO).isNull();
    }

    @Test
    public void testGetResidentsMedicalHistoryFromAddressWhenPersonListIsEmpty_ShouldReturnNull() {
        when(personRepository.findAllByAddress(any())).thenReturn(new ArrayList<>());

        FireDTO returnedFireDTO = urlService.getResidentsMedicalHistoryFromAddress("644 Gershwin Cir");
        verify(medicalRecordRepository, Mockito.times(0)).findAllByNames(any());
        assertThat(returnedFireDTO).isNull();
    }

    @Test
    public void testGetResidentsMedicalHistoryFromAddressWhenMedicalRecordListIsEmpty_ShouldReturnNull() {
        when(personRepository.findAllByAddress(any())).thenReturn(personList1);
        when(firestationRepository.findOneByAddress(any())).thenReturn(firestation1);
        when(medicalRecordRepository.findAllByNames(any())).thenReturn(new ArrayList<>());

        FireDTO returnedFireDTO = urlService.getResidentsMedicalHistoryFromAddress("644 Gershwin Cir");
        verify(medicalRecordRepository, Mockito.times(1)).findAllByNames(any());
        assertThat(returnedFireDTO).isNull();
    }


    // test getHomeListFromFirestationNumbers

    @Test
    public void testGetHomeListFromFirestationNumbers_ShouldReturnFloodDTOList() {
        List<MedicalRecord> medicalRecordList = new ArrayList<MedicalRecord>(List.of(new MedicalRecord("Peter", "Duncan", "09/06/2000", List.of(), List.of("shellfish"))));

        when(firestationRepository.findAllByStationNumber(any())).thenReturn(addressList2);
        when(personRepository.findAllByAddress(any())).thenReturn(personList1);
        when(medicalRecordRepository.findAllByNames(any())).thenReturn(medicalRecordList);

        List<FloodDTO> returnedFloodDTOList = urlService.getHomeListFromFirestationNumbers(List.of("1"));
        assertEquals(returnedFloodDTOList.get(0).getStation(), "1");
        assertEquals(returnedFloodDTOList.get(0).getFloodAddressDTOList().get(0).getAddress(), "644 Gershwin Cir");
        assertEquals(returnedFloodDTOList.get(0).getFloodAddressDTOList().get(0).getPersonMedicalHistoryDTOList().size(), 1);
        assertEquals(returnedFloodDTOList.get(0).getFloodAddressDTOList().get(0).getPersonMedicalHistoryDTOList().get(0).getLastName(), "Duncan");
    }

    @Test
    public void testGetHomeListFromFirestationNumbersWhenPersonListIsEmpty_ShouldReturnNull() {
        when(firestationRepository.findAllByStationNumber(any())).thenReturn(addressList2);
        when(personRepository.findAllByAddress(any())).thenReturn(new ArrayList<>());

        List<FloodDTO> returnedFloodDTOList = urlService.getHomeListFromFirestationNumbers(List.of("1"));
        assertThat(returnedFloodDTOList).isNull();
    }

    @Test
    public void testGetHomeListFromFirestationNumbersWhenMedicalRecordListIsEmpty_ShouldReturnNull() {
        when(firestationRepository.findAllByStationNumber(any())).thenReturn(addressList2);
        when(personRepository.findAllByAddress(any())).thenReturn(personList1);
        when(medicalRecordRepository.findAllByNames(any())).thenReturn(new ArrayList<>());

        List<FloodDTO> returnedFloodDTOList = urlService.getHomeListFromFirestationNumbers(List.of("1"));
        assertThat(returnedFloodDTOList).isNull();
    }


    // test getPhoneListFromFirestationNumber

    @Test
    public void testGetPhoneListFromFirestationNumber_ShouldReturnFirestationNumber() {
        when(firestationRepository.findAllByStationNumber(any())).thenReturn(addressList1);
        when(personRepository.findAllByAddressList(any())).thenReturn(firestationPersonDTOList1);

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
        when(firestationRepository.findAllByStationNumber(any())).thenReturn(addressList1);
        when(personRepository.findAllByAddressList(any())).thenReturn(new ArrayList<>());

        List<String> returnedPhoneList = urlService.getPhoneListFromFirestationNumber("1");
        verify(personRepository, Mockito.times(1)).findAllByAddressList(any());
        assertThat(returnedPhoneList).isEmpty();
    }


    // test getInformationAndMedicalHistoryFromFullName

    @Test
    public void testGetInformationAndMedicalHistoryFromFullName_ShouldReturnListPersonInfoDTO() {
        when(personRepository.findAllByFullName(any(), any())).thenReturn(personList1);
        when(medicalRecordRepository.findAllByNames(any())).thenReturn(medicalRecordList2);

        List<PersonInfoDTO> returnedPersonInfoDTOList = urlService.getInformationAndMedicalHistoryFromFullName("Peter", "Duncan");
        verify(medicalRecordRepository, Mockito.times(1)).findAllByNames(any());
        assertEquals(returnedPersonInfoDTOList.size(), 1);
    }

    @Test
    public void testGetInformationAndMedicalHistoryFromFullNameWhenPersonListIsEmpty_ShouldReturnEmptyList() {
        when(personRepository.findAllByFullName(any(), any())).thenReturn(new ArrayList<>());

        List<PersonInfoDTO> returnedPersonInfoDTOList = urlService.getInformationAndMedicalHistoryFromFullName("Peter", "Duncan");
        assertThat(returnedPersonInfoDTOList).isNull();
    }

    @Test
    public void testGetInformationAndMedicalHistoryFromFullNameWhenMedicalRecordListIsEmpty_ShouldReturnEmptyList() {
        when(personRepository.findAllByFullName(any(), any())).thenReturn(personList1);
        when(medicalRecordRepository.findAllByNames(any())).thenReturn(new ArrayList<>());

        List<PersonInfoDTO> returnedPersonInfoDTOList = urlService.getInformationAndMedicalHistoryFromFullName("Peter", "Duncan");
        verify(medicalRecordRepository, Mockito.times(1)).findAllByNames(any());
        assertThat(returnedPersonInfoDTOList).isNull();
    }


    // test getAllEmailsFromCity

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
