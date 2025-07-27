package com.personalApp.personal_service.business.responses;

import com.personalApp.shared_model.enums.Seniority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetByPositionEmployeeResponse {

    private int id;
    private String firstName;
    private String lastName;
    private String identityNumber;
    private String email;
    private String phoneNumber;
    private String address;
    private double salary;
//    private LocalDate dateOfBirth;
//    private LocalDate hireDate;
//    private LocalDate exitDate;
    private String gender;
    private String position;
//    private String seniority;
    private Seniority seniority;

    private String departmantName;
    private String companyName;
}
