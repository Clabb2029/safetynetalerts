package com.safetynet.safetynetalerts.DTO;

import lombok.Data;

import java.util.List;

@Data
public class HomeMemberDTO {
    private List<HomeChildDTO> homeChildDTOList;
    private List<HomeOtherMemberDTO> homeOtherMemberDTOList;
}
