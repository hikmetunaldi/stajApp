package com.personalApp.personal_service.webApi.controllers;

import com.personalApp.personal_service.business.abstracts.EmployeeService;
import com.personalApp.personal_service.business.requests.CreateEmployeeRequest;
import com.personalApp.personal_service.business.requests.UpdateEmployeeRequest;
import com.personalApp.personal_service.business.responses.*;
import com.personalApp.personal_service.entities.concretes.Employee;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private EmployeeService employeeService;


    @GetMapping
    public List<GetAllEmployeeResponse> getAll() {
        return employeeService.getAll();
    }

    @GetMapping("/{id}")
    public GetByIdEmployeeResponse getById(@PathVariable int id) {
        return employeeService.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void add(@RequestBody CreateEmployeeRequest createEmployeeRequest) {
        employeeService.add(createEmployeeRequest);
    }

    @PutMapping
    public void update(@RequestBody UpdateEmployeeRequest updateEmployeeRequest) {
        employeeService.update(updateEmployeeRequest);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        employeeService.delete(id);
    }


    @GetMapping("/find/by-identity/{identityNumber}")
    public FindEmployeesByIdentityNumber findByIdentityNumber(@RequestParam String identityNumber) {
        return employeeService.findByIdentityNumber(identityNumber);
    }

    @GetMapping("/find/by-department/{departmentId}")
    public List<FindEmployeesByDepartmentResponse> findByDepartmentId(@PathVariable int departmentId) {
        return employeeService.findEmployeesByDepartmentId(departmentId);
    }

    @GetMapping("/find/by-company/{companyId}")
    public List<FindEmployeesByCompanyResponse> findByCompanyId(@PathVariable int companyId){
        return employeeService.findEmployeesByCompanyId(companyId);
    }


    @GetMapping("/by-firstName-lastName")
    public List<FindEmployeesByFirstNameAndLastName> findByFirstNameAndLastName(@RequestParam(required = false) String firstName,
                                                                                @RequestParam(required = false) String lastName) {
        return employeeService.findByFirstNameAndLastName(setIfNameIsNullOrEmpty(firstName), setIfNameIsNullOrEmpty(lastName));
    }

    private String setIfNameIsNullOrEmpty(String str) {
        if (str == null || str.isEmpty()) {
            return " ";
        }
        return str;
    }
}
