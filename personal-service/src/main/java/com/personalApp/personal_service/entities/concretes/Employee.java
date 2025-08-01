package com.personalApp.personal_service.entities.concretes;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.personalApp.shared_model.converters.SeniorityConverter;
import com.personalApp.shared_model.enums.Seniority;
import jakarta.persistence.*;
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

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "identity_number", unique = true)
    private String identityNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "salary")
    private double salary;

    @Column(name = "gender")
    private String gender;

    @Column(name = "position")
    private String position;

    // Sadece bir seniority field'i - Enum kullanın
    @Convert(converter = SeniorityConverter.class)
    @Column(name = "seniority")
    private Seniority seniority;

    // Department ile ilişki - Düzeltilmiş
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @JsonBackReference(value = "department-employees")
    private Department department;

    // Company ile ilişki - Düzeltilmiş
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @JsonBackReference(value = "company-employees")
    private Company company;
}
