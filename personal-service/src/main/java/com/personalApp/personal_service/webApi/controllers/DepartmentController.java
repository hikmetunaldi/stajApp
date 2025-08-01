package com.personalApp.personal_service.webApi.controllers;

import com.personalApp.personal_service.business.abstracts.DepartmentService;
import com.personalApp.personal_service.business.requests.CreateDepartmentRequest;
import com.personalApp.personal_service.business.requests.UpdateDepartmentRequest;
import com.personalApp.personal_service.business.responses.GetAllDepartmentResponse;
import com.personalApp.personal_service.business.responses.GetByIdDepartmentResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private DepartmentService departmentService;

    @GetMapping
    public List<GetAllDepartmentResponse> getAll() {
        return departmentService.getAll();
    }

    @GetMapping("/{id}")
    public GetByIdDepartmentResponse getById(@PathVariable int id) {
        return departmentService.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void add(@RequestBody CreateDepartmentRequest createDepartmentRequest) {
        departmentService.add(createDepartmentRequest);
    }

    @PutMapping
    public void update(@RequestBody UpdateDepartmentRequest updateDepartmentRequest) {
        departmentService.update(updateDepartmentRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        departmentService.delete(id);
    }

}
