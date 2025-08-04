package com.personalApp.personal_service.webApi.controllers;

import com.personalApp.personal_service.business.abstracts.CompanyService;
import com.personalApp.personal_service.business.requests.CreateCompanyRequest;
import com.personalApp.personal_service.business.requests.UpdateCompanyRequest;
import com.personalApp.personal_service.business.responses.GetAllCompanyResponse;
import com.personalApp.personal_service.business.responses.GetByIdCompanyResponse;
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
@RequestMapping("/api/companies")
@Tag(name = "Company", description = "Company management APIs")
@Validated
public class CompanyController {

    private CompanyService companyService;

    @GetMapping
    public List<GetAllCompanyResponse> getAll() {
        return companyService.getAll();
    }

    @GetMapping("/{id}")
    public GetByIdCompanyResponse getById(
            @PathVariable @Min(1) @NotNull int id) {
        return companyService.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void add(@RequestBody @Valid CreateCompanyRequest createCompanyRequest) {
        companyService.add(createCompanyRequest);
    }

    @PutMapping
    public void update(@RequestBody @Valid UpdateCompanyRequest updateCompanyRequest) {
        companyService.update(updateCompanyRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Min(1) @NotNull int id) {
        companyService.delete(id);
    }


}
