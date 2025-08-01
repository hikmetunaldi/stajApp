package com.personalApp.personal_service.business.responses;

import com.personalApp.shared_model.enums.Seniority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetByIdEmployeeResponse {

    private int id;
    private String firstName;
    private String lastName;
    private String identityNumber;
    private String email;
    private String phoneNumber;
    private String address;
    private double salary;
    private String gender;
    private String position;
    private Seniority seniority;

    private String departmentName;
    private String companyName;
}
