package com.personalApp.personal_service.business.requests;

import com.personalApp.shared_model.enums.Seniority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeRequest {

    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String lastName;

    @NotNull
    @NotBlank
    @Size(min = 11, max = 11)
    private String identityNumber;

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String phoneNumber;

    private String address;
    private double salary;
    private String gender;
    private String position;
    private Seniority seniority;

    @NotNull
    private Integer departmentId;

    @NotNull
    private Integer companyId;


}
