package com.personalApp.personal_service.dataAccess.abstracts;

import com.personalApp.personal_service.entities.concretes.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findByIdentityNumber(String identityNumber);

    List<Employee> findByFirstNameAndLastName(String firstName, String lastName);
}
