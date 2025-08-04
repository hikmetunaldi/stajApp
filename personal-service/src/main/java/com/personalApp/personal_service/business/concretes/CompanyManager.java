package com.personalApp.personal_service.business.concretes;

import com.personalApp.personal_service.business.abstracts.CompanyService;
import com.personalApp.personal_service.business.requests.CreateCompanyRequest;
import com.personalApp.personal_service.business.requests.UpdateCompanyRequest;
import com.personalApp.personal_service.business.responses.GetAllCompanyResponse;
import com.personalApp.personal_service.business.responses.GetByIdCompanyResponse;
import com.personalApp.personal_service.core.utilities.mappers.ModelMapperService;
import com.personalApp.personal_service.dataAccess.abstracts.CompanyRepository;
import com.personalApp.personal_service.entities.concretes.Company;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class CompanyManager implements CompanyService {

    private CompanyRepository companyRepository;
    private ModelMapperService modelMapperService;

    public List<GetAllCompanyResponse> getAll() {
        List<Company> companies = companyRepository.findAll();

        return companies.stream().map(Company -> modelMapperService.forResponse()
                        .map(Company, GetAllCompanyResponse.class))
                .sorted(Comparator.comparing(GetAllCompanyResponse::getId)).toList();
    }

    public GetByIdCompanyResponse getById(int id) {
        Company company = companyRepository.findById(id).orElseThrow();
        return modelMapperService.forResponse().map(company, GetByIdCompanyResponse.class);
    }

    @Override
    public void add(CreateCompanyRequest createCompanyRequest) {
        Company company = this.modelMapperService.forRequest().map(createCompanyRequest, Company.class);
        companyRepository.save(company);
    }

    public void update(UpdateCompanyRequest updateCompanyRequest) {
        Company company = this.modelMapperService.forRequest().map(updateCompanyRequest, Company.class);

        companyRepository.save(company);
    }

    public void delete(int id) {
        companyRepository.deleteById(id);
    }
}
