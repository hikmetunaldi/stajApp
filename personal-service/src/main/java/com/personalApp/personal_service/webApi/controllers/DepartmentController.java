package com.personalApp.personal_service.webApi.controllers;

import com.personalApp.personal_service.business.abstracts.DepartmentService;
import com.personalApp.personal_service.business.requests.CreateDepartmentRequest;
import com.personalApp.personal_service.business.requests.UpdateDepartmentRequest;
import com.personalApp.personal_service.business.responses.GetAllDepartmentResponse;
import com.personalApp.personal_service.business.responses.GetByIdDepartmentResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@RestController
@RequestMapping("/api/departments")
@Tag(name = "Department", description = "Department management APIs")
@Validated
public class DepartmentController {

    private DepartmentService departmentService;

    @GetMapping
    public List<GetAllDepartmentResponse> getAll() {
        return departmentService.getAll();
    }

    @GetMapping("/{id}")
    public GetByIdDepartmentResponse getById(
            @PathVariable @Min(1) @NotNull int id) {
        return departmentService.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void add(@RequestBody @Valid CreateDepartmentRequest createDepartmentRequest) {
        departmentService.add(createDepartmentRequest);
    }

    @PutMapping
    public void update(@RequestBody @Valid UpdateDepartmentRequest updateDepartmentRequest) {
        departmentService.update(updateDepartmentRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Min(1) @NotNull int id) {
        departmentService.delete(id);
    }

}
