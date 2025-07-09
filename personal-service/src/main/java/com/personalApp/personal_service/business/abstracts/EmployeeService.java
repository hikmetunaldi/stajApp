package com.personalApp.personal_service.business.abstracts;

import com.personalApp.personal_service.business.requests.CreateEmployeeRequest;
import com.personalApp.personal_service.business.requests.UpdateEmployeeRequest;
import com.personalApp.personal_service.business.responses.GetAllEmployeeResponse;
import com.personalApp.personal_service.business.responses.GetByIdEmployeeResponse;
import com.personalApp.personal_service.entities.concretes.Employee;

import java.util.List;

public interface EmployeeService {

    List<GetAllEmployeeResponse> getAll();
    GetByIdEmployeeResponse getById(int id);
    //GetByPositionEmployeeResponse getByPosition(String position);
    void add(CreateEmployeeRequest createEmployeeRequest);
    void update(UpdateEmployeeRequest updateEmployeeRequest);
    void delete(int id);
    Employee findByIdentityNumber(String identityNumber);
    List<Employee> findByFirstNameAndLastName(String firstName, String lastName);
}
