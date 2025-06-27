package com.personalApp.personal_service.business.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private LocalDate dateOfBirth;
    private LocalDate hireDate;
    private LocalDate exitDate;
    private String gender;
    private String position;
    private String seniority;
    private int seniorityy;

    @NotNull
    @NotBlank
    private int departmentId;

    @NotNull
    @NotBlank
    private int companyId;
}
