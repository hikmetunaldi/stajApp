package com.personalApp.personal_service.business.requests;

import com.personalApp.shared_model.enums.Gender;
import com.personalApp.shared_model.enums.Seniority;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeRequest {

    @NotBlank
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 50)
    private String lastName;

    @NotBlank
    @Size(min = 11, max = 11)
    private String identityNumber;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phoneNumber;

    private String address;
    @Positive
    private double salary;

    private Gender gender;
    private String position;
    private Seniority seniority;

    @Positive
    private Integer departmentId;

    @Positive
    private Integer companyId;

}
