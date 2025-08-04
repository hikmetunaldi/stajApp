package com.personalApp.personal_service.business.requests;

import com.personalApp.shared_model.enums.Gender;
import com.personalApp.shared_model.enums.Seniority;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeRequest {

    @Positive
    private int id;

    @Size(min = 2, max = 50)
    private String firstName;

    @Size(min = 2, max = 50)
    private String lastName;
    @NumberFormat
    private String identityNumber;

    @Email
    private String email;

    private String phoneNumber;

    private String address;

    @Positive
    private Double salary;

    private Gender gender;
    private String position;
    private Seniority seniority;

    @Positive
    private Integer departmentId;

    @Positive
    private Integer companyId;
}
