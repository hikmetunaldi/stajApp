package com.personalApp.personal_service.business.abstracts;

import com.personalApp.personal_service.business.requests.CreateCompanyRequest;
import com.personalApp.personal_service.business.requests.UpdateCompanyRequest;
import com.personalApp.personal_service.business.responses.GetAllCompanyResponse;
import com.personalApp.personal_service.business.responses.GetByIdCompanyResponse;
import com.personalApp.personal_service.entities.concretes.Company;

import java.util.List;

public interface CompanyService {
    List<GetAllCompanyResponse> getAll();

    GetByIdCompanyResponse getById(int id);

    void add(CreateCompanyRequest createCompanyRequest);

    void update(UpdateCompanyRequest updateCompanyRequest);

    void delete(int id);

    Company getCompanyById(int id);
}
