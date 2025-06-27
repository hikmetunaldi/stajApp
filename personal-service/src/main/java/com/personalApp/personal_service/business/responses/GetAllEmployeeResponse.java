package com.personalApp.personal_service.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllEmployeeResponse {

    private int id;
    private String firstName;
    private String lastName;
    private String identityNumber;
    private String email;
    private String phoneNumber;
    private String address;
    private double salary;
    private LocalDate dateOfBirth;
    private LocalDate hireDate;
    private LocalDate exitDate;
    private String gender;
    private String position;
    private String seniority;

    private String departmentName;

}
