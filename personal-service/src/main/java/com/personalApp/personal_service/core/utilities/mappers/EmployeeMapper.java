package com.personalApp.personal_service.core.utilities.mappers;

import com.personalApp.personal_service.business.requests.CreateEmployeeRequest;
import com.personalApp.personal_service.dataAccess.abstracts.CompanyRepository;
import com.personalApp.personal_service.dataAccess.abstracts.DepartmentRepository;
import com.personalApp.personal_service.entities.concretes.Company;
import com.personalApp.personal_service.entities.concretes.Department;
import com.personalApp.personal_service.entities.concretes.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class EmployeeMapper {

    @Mapping(source = "companyId", target = "company")
    @Mapping(source = "departmentId", target = "department")
    public abstract Employee toEntity(CreateEmployeeRequest dto);


    protected Company mapCompany(Integer id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + id));
    }


    protected Department mapDepartment(Integer id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + id));
    }


    @Autowired
    protected CompanyRepository companyRepository;

    @Autowired
    protected DepartmentRepository departmentRepository;
}
