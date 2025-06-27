package com.personalApp.personal_service.webApi.controllers;

import com.personalApp.personal_service.business.abstracts.CompanyService;
import com.personalApp.personal_service.business.requests.CreateCompanyRequest;
import com.personalApp.personal_service.business.requests.UpdateCompanyRequest;
import com.personalApp.personal_service.business.responses.GetAllCompanyResponse;
import com.personalApp.personal_service.business.responses.GetByIdCompanyResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/companys")
public class CompanyController {

    private CompanyService companyService;

    @GetMapping
    public List<GetAllCompanyResponse> getALL(){
        return companyService.getAll();
    }

    @GetMapping("/{id}")
    public GetByIdCompanyResponse getById(@PathVariable int id){
        return companyService.getById(id);
    }

    @PostMapping
    @ResponseStatus(code=HttpStatus.CREATED)
    public void add(@RequestBody CreateCompanyRequest createCompanyRequest){
        this.companyService.add(createCompanyRequest);
    }

    @PutMapping
    public void update(@RequestBody UpdateCompanyRequest updateCompanyRequest){
        this.companyService.update(updateCompanyRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id){
        this.companyService.delete(id);
    }


}
