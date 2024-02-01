package com.safetynet.safetynetalerts.DTO;

import lombok.Data;

import java.util.List;

@Data
public class FloodAddressDTO {
    private String address;
    List<FirePersonDTO> personMedicalHistoryDTOList;

    public FloodAddressDTO() {}

    public FloodAddressDTO(String address, List<FirePersonDTO> personMedicalHistoryDTOList) {
        this.address = address;
        this.personMedicalHistoryDTOList = personMedicalHistoryDTOList;
    }
}
