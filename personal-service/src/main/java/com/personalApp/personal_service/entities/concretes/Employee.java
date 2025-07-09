package com.personalApp.personal_service.entities.concretes;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.personalApp.shared_model.enums.Seniority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name="employees")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="firstName")
    private String firstName;
    @Column(name="lastName")
    private String lastName;
    @Column(name="identityNumber",unique = true)
    private String identityNumber;
    @Column(name="email")
    private String email;
    @Column(name="phoneNumber")
    private String phoneNumber;
    @Column(name="address")
    private String address;
    @Column(name="salary")
    private double salary;
    @Column(name="gender")
    private String gender;
    @Column(name="position")
    private String position;
    @Column(name="seniority")
    private String seniority;
    @Column
    private Seniority seniorityy;

    @ManyToOne
    @JsonBackReference(value = "company-emplooyes")
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JsonBackReference(value = "department-emplooyes")
    @JoinColumn(name="company_id")
    private Company company;

}
