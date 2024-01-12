package com.safetynet.safetynetalerts.DTO;

import lombok.Data;

import java.util.List;

@Data
public class HomeDTO {

    private String address;
    List<PersonMedicalHistoryDTO> personMedicalHistoryDTOList;
}
