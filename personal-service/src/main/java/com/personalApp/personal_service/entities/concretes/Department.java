package com.personalApp.personal_service.entities.concretes;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Table(name = "departments")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false, length = 100)
    @NotBlank(message = "Department name cannot be blank")
    @Size(min = 2, max = 100, message = "Department name must be between 2 and 100 characters")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "company-departments")
    @JoinColumn(name = "company_id", nullable = false)
    @NotNull(message = "Company cannot be null")
    private Company company;

    @OneToMany(mappedBy = "department")
    @JsonManagedReference(value = "department-employees")
    private List<Employee> employees;

}
