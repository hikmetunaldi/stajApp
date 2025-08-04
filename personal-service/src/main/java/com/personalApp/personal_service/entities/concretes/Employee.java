package com.personalApp.personal_service.entities.concretes;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.personalApp.shared_model.converters.GenderConverter;
import com.personalApp.shared_model.converters.SeniorityConverter;
import com.personalApp.shared_model.enums.Gender;
import com.personalApp.shared_model.enums.Seniority;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "employees")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "first_name", nullable = false, length = 50)
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Column(name = "identity_number", unique = true, nullable = false, length = 11)
    @NotBlank(message = "Identity number cannot be blank")
    @Size(min = 11, max = 11, message = "Identity number must be 11 characters")
    private String identityNumber;

    @Column(name = "email", nullable = false, length = 100)
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address", length = 500)
    @Size(max = 500, message = "Address cannot exceed 500 characters")
    private String address;

    @Column(name = "salary", nullable = false)
    @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be greater than 0")
    @DecimalMax(value = "999999.99", message = "Salary cannot exceed 999999.99")
    private double salary;

    @Column(name = "gender", nullable = false)
    @Convert(converter = GenderConverter.class)
    @NotNull(message = "Gender cannot be null")
    private Gender gender;

    @Column(name = "position", nullable = false, length = 100)
    @NotBlank(message = "Position cannot be blank")
    @Size(min = 2, max = 100, message = "Position must be between 2 and 100 characters")
    private String position;

    @Convert(converter = SeniorityConverter.class)
    @Column(name = "seniority", nullable = false)
    @NotNull(message = "Seniority cannot be null")
    private Seniority seniority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    @JsonBackReference(value = "department-employees")
    @NotNull(message = "Department cannot be null")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @JsonBackReference(value = "company-employees")
    @NotNull(message = "Company cannot be null")
    private Company company;
}
