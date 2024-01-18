package com.safetynet.safetynetalerts.DTO;

import lombok.Data;

import java.util.List;

@Data
public class ChildAlertDTO {
    private List<ChildAlertHomeMemberDTO> childrenList;
    private List<ChildAlertHomeMemberDTO> adultList;
}
