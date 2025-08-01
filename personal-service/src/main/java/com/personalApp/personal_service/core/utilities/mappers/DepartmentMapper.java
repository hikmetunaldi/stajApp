package com.personalApp.personal_service.core.utilities.mappers;

import com.personalApp.personal_service.business.requests.CreateDepartmentRequest;
import com.personalApp.personal_service.dataAccess.abstracts.CompanyRepository;
import com.personalApp.personal_service.entities.concretes.Company;
import com.personalApp.personal_service.entities.concretes.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class DepartmentMapper {

    @Mapping(source = "companyId", target = "company")
    public abstract Department toEntity(CreateDepartmentRequest dto);


    protected Company mapCompany(Integer id) {
        return companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company not found with ID:" + id));
    }


    protected CompanyRepository companyRepository;
}
