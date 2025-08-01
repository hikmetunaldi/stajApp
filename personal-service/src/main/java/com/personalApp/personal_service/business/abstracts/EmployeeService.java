package com.personalApp.personal_service.business.abstracts;

import com.personalApp.personal_service.business.requests.CreateEmployeeRequest;
import com.personalApp.personal_service.business.requests.UpdateEmployeeRequest;
import com.personalApp.personal_service.business.responses.*;
import com.personalApp.personal_service.entities.concretes.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    List<GetAllEmployeeResponse> getAll();

    GetByIdEmployeeResponse getById(int id);

    void add(CreateEmployeeRequest createEmployeeRequest);

    void update(UpdateEmployeeRequest updateEmployeeRequest);

    void delete(int id);

    FindEmployeesByIdentityNumber findByIdentityNumber(String identityNumber);

    List<FindEmployeesByFirstNameAndLastName> findByFirstNameAndLastName(String firstName, String lastName);

    List<FindEmployeesByDepartmentResponse> findEmployeesByDepartmentId(int departmentId);

    List<FindEmployeesByCompanyResponse> findEmployeesByCompanyId(int companyId);
}
