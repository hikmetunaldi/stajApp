package com.personalApp.personal_service.webApi.controllers;

import com.personalApp.personal_service.business.abstracts.EmployeeService;
import com.personalApp.personal_service.business.requests.CreateEmployeeRequest;
import com.personalApp.personal_service.business.requests.UpdateEmployeeRequest;
import com.personalApp.personal_service.business.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee", description = "Employee management APIs")
@Validated
public class EmployeeController {

    private EmployeeService employeeService;


    @GetMapping
    public List<GetAllEmployeeResponse> getAll() {
        return employeeService.getAll();
    }

    @GetMapping("/{id}")
    public GetByIdEmployeeResponse getById(@PathVariable @Min(1) @NotNull int id) {
        return employeeService.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void add(@RequestBody @Valid CreateEmployeeRequest createEmployeeRequest) {
        employeeService.add(createEmployeeRequest);
    }

    @PutMapping
    public void update(@RequestBody @Valid UpdateEmployeeRequest updateEmployeeRequest) {
        employeeService.update(updateEmployeeRequest);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Min(1) @NotNull int id) {
        employeeService.delete(id);
    }


    @GetMapping("/find/by-identity/{identityNumber}")
    public List<FindEmployeesByIdentityNumber> findByIdentityNumber(
            @PathVariable @NotBlank @Size(min = 11, max = 11) String identityNumber) {
        FindEmployeesByIdentityNumber employee = employeeService.findByIdentityNumber(identityNumber);
        return List.of(employee);
    }

    @GetMapping("/find/by-department/{departmentId}")
    public List<FindEmployeesByDepartmentResponse> findByDepartmentId(
            @PathVariable @Min(1) @NotNull int departmentId) {
        return employeeService.findEmployeesByDepartmentId(departmentId);
    }

    @GetMapping("/find/by-company/{companyId}")
    public List<FindEmployeesByCompanyResponse> findByCompanyId(
            @PathVariable @Min(1) @NotNull int companyId){
        return employeeService.findEmployeesByCompanyId(companyId);
    }

    @GetMapping("/by-company/{companyId}/department/{departmentId}")
    public List<FindEmployeesByCompanyIdAndDepartmentId> findByCompanyIdAndDepartmentId(
            @PathVariable @Min(1) @NotNull int companyId,
            @PathVariable @Min(1) @NotNull int departmentId){
        return employeeService.findEmployeesByCompanyIdAndDepartmentId(companyId, departmentId);
    }

    @GetMapping("/by-firstName")
    public List<FindEmployeesByFirstName> findEmployeesByFirstName(
            @RequestParam @NotBlank @Size(min = 2, max = 50) String firstName) {
        return employeeService.findEmployeesByFirstName(firstName);
    }

    @GetMapping("/by-lastName")
    public List<FindEmployeesByLastName> findEmployeesByLastName(
            @RequestParam @NotBlank @Size(min = 2, max = 50) String lastName) {
        return employeeService.findEmployeesByLastName(lastName);
    }

    @GetMapping("/by-firstName-lastName")
    public List<FindEmployeesByFirstNameAndLastName> findByFirstNameAndLastName(
            @RequestParam(required = false) @Size(min = 2, max = 50) String firstName,
            @RequestParam(required = false) @Size(min = 2, max = 50) String lastName) {
        return employeeService.findByFirstNameAndLastName(setIfNameIsNullOrEmpty(firstName), setIfNameIsNullOrEmpty(lastName));
    }

    private String setIfNameIsNullOrEmpty(String str) {
        if (str == null || str.isEmpty()) {
            return " ";
        }
        return str;
    }
}
