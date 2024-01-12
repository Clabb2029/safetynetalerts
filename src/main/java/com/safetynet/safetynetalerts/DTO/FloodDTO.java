package com.safetynet.safetynetalerts.DTO;

import lombok.Data;

import java.util.List;

@Data
public class FloodDTO {

    private String station;
    List<HomeDTO> homeDTOList;
}
