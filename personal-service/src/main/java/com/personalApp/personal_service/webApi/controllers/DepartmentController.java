package com.personalApp.personal_service.webApi.controllers;

import com.personalApp.personal_service.business.abstracts.DepartmentService;
import com.personalApp.personal_service.business.requests.CreateDepartmentRequest;
import com.personalApp.personal_service.business.requests.UpdateDepartmentRequest;
import com.personalApp.personal_service.business.responses.GetAllDepartmentResponse;
import com.personalApp.personal_service.business.responses.GetByIdDepartmantResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/departmants")
public class DepartmentController {

    private DepartmentService departmentService;

    @GetMapping
    public List<GetAllDepartmentResponse> getAll(){
        return departmentService.getAll();
    }

    @GetMapping("/{id}")
    public GetByIdDepartmantResponse getById(@PathVariable int id){
        return departmentService.getById(id);
    }

    @PostMapping
    @ResponseStatus(code= HttpStatus.CREATED)
    public void add(@RequestBody CreateDepartmentRequest createDepartmentRequest){
        this.departmentService.add(createDepartmentRequest);
    }

    @PutMapping
    public void update(@RequestBody UpdateDepartmentRequest updateDepartmentRequest){
        this.departmentService.update(updateDepartmentRequest);
    }

    @DeleteMapping("/{id}")
    public void delete (@PathVariable int id){
        this.departmentService.delete(id);
    }

}
