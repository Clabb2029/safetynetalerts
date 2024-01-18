package com.safetynet.safetynetalerts.DTO;

import lombok.Data;

import java.util.List;

@Data
public class FireDTO {
    private List<FirePersonDTO> personMedicalHistoryDTOList;
    private String station;
}
