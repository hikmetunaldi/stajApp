package com.personalApp.personal_service.business.requests;

import com.personalApp.shared_model.enums.Seniority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeRequest {

    private int id;
    private String firstName;
    private String lastName;
    private String identityNumber;
    private String email;
    private String phoneNumber;
    private String address;
    private Double salary;
//    private LocalDate dateOfBirth;
//    private LocalDate hireDate;
//    private LocalDate exitDate;
    private String gender;
    private String position;
//    private String seniority;
//    private int seniorityy;
    private Seniority seniority;

    private Integer departmentId;
    private Integer companyId;
}
