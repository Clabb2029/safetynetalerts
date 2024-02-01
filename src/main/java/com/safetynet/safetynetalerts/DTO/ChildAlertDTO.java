package com.safetynet.safetynetalerts.DTO;

import lombok.Data;

import java.util.List;

@Data
public class ChildAlertDTO {
    private List<ChildAlertHomeMemberDTO> childrenList;
    private List<ChildAlertHomeMemberDTO> adultList;

    public ChildAlertDTO() {}

    public ChildAlertDTO(List<ChildAlertHomeMemberDTO> childrenList, List<ChildAlertHomeMemberDTO> adultList) {
        this.childrenList = childrenList;
        this.adultList = adultList;
    }
}
