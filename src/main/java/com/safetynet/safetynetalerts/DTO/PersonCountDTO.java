package com.safetynet.safetynetalerts.DTO;

import lombok.Data;

import java.util.List;

@Data
public class PersonCountDTO {

    private List<PersonDTO> personDTOList;
    private int adultCount;
    private int childrenCount;

}
