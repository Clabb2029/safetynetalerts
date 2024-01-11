package com.safetynet.safetynetalerts.DTO;

import lombok.Data;

import java.util.List;

@Data
public class PersonMedicalHistoryListDTO {

    private List<PersonMedicalHistoryDTO> personMedicalHistoryDTOList;
    private String station;
}
