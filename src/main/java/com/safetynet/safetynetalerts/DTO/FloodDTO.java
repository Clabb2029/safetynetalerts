package com.safetynet.safetynetalerts.DTO;

import lombok.Data;

import java.util.List;

@Data
public class FloodDTO {
    private String station;
    List<FloodAddressDTO> floodAddressDTOList;

    public FloodDTO() {}

    public FloodDTO(String station, List<FloodAddressDTO> floodAddressDTOList) {
        this.station = station;
        this.floodAddressDTOList = floodAddressDTOList;
    }
}
