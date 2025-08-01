package com.personalApp.personal_service.dataAccess.abstracts;

import com.personalApp.personal_service.business.responses.FindEmployeesByIdentityNumber;
import com.personalApp.personal_service.entities.concretes.Company;
import com.personalApp.personal_service.entities.concretes.Department;
import com.personalApp.personal_service.entities.concretes.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Optional<Employee> findByIdentityNumber(String identityNumber);

    Optional<List<Employee>> findByFirstNameAndLastName(String firstName, String lastName);

    List<Employee> findEmployeesByDepartment(Department department);

    List<Employee> findEmployeesByCompany(Company company);
}
