package com.personalApp.personal_service.entities.concretes;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "company")
    @JsonManagedReference(value = "company-departments")
    private List<Department> departments;

    @OneToMany(mappedBy = "company")
    @JsonManagedReference(value = "company-employees")
    private List<Employee> employees;
}
