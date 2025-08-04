package com.personalApp.personal_service.entities.concretes;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "companies")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    @NotBlank(message = "Company name cannot be blank")
    @Size(min = 2, max = 100, message = "Company name must be between 2 and 100 characters")
    private String name;

    @OneToMany(mappedBy = "company")
    @JsonManagedReference(value = "company-departments")
    private List<Department> departments;

    @OneToMany(mappedBy = "company")
    @JsonManagedReference(value = "company-employees")
    private List<Employee> employees;
}
