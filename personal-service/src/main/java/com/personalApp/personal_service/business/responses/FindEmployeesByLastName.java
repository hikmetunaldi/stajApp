package com.personalApp.personal_service.business.responses;

import com.personalApp.shared_model.enums.Gender;
import com.personalApp.shared_model.enums.Seniority;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindEmployeesByLastName {
    private Integer id;
    private String firstName;
    private String lastName;
    private String identityNumber;
    private String email;
    private String phoneNumber;
    private String address;
    private Double salary;
    private Gender gender;
    private String position;
    private Seniority seniority;
    private String departmentName;
    private String companyName;
}
