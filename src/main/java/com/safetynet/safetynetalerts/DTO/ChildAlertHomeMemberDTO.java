package com.safetynet.safetynetalerts.DTO;

import lombok.Data;

@Data
public class ChildAlertHomeMemberDTO {
    private String firstName;
    private String lastName;
    private int age;

    public ChildAlertHomeMemberDTO() {}

    public ChildAlertHomeMemberDTO(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
}
