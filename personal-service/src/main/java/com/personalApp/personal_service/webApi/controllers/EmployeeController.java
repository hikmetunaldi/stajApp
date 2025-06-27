package com.personalApp.personal_service.webApi.controllers;

import com.personalApp.personal_service.business.abstracts.EmployeeService;
import com.personalApp.personal_service.business.requests.CreateEmployeeRequest;
import com.personalApp.personal_service.business.requests.UpdateEmployeeRequest;
import com.personalApp.personal_service.business.responses.GetAllEmployeeResponse;
import com.personalApp.personal_service.business.responses.GetByIdEmployeeResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private EmployeeService employeeService;


    @GetMapping
    public List<GetAllEmployeeResponse> getAll(){
        return employeeService.getAll();
    }

    @GetMapping("/{id}")
    public GetByIdEmployeeResponse getById(@PathVariable int id){
        return employeeService.getById(id);
    }

    @PostMapping
    @ResponseStatus(code= HttpStatus.CREATED)
    public void add(CreateEmployeeRequest createEmployeeRequest ){
        this.employeeService.add(createEmployeeRequest);
    }

    @PutMapping
    public void update(@RequestBody UpdateEmployeeRequest updateEmployeeRequest ){
        this.employeeService.update(updateEmployeeRequest);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id){
        this.employeeService.delete(id);
    }

}
