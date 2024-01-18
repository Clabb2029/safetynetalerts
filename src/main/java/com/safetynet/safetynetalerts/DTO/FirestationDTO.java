package com.safetynet.safetynetalerts.DTO;

import lombok.Data;

import java.util.List;

@Data
public class FirestationDTO {
    private List<FirestationPersonDTO> firestationPersonDTOList;
    private int adultCount;
    private int childrenCount;
}
