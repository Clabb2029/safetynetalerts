package com.safetynet.safetynetalerts.DTO;

import lombok.Data;

@Data
public class FirestationPersonDTO {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;

    public FirestationPersonDTO() {}

    public FirestationPersonDTO(String firstName, String lastName, String address, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
    }
}
