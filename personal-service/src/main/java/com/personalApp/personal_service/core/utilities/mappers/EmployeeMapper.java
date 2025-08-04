package com.personalApp.personal_service.core.utilities.mappers;

import com.personalApp.personal_service.business.requests.CreateEmployeeRequest;
import com.personalApp.personal_service.business.requests.UpdateEmployeeRequest;
import com.personalApp.personal_service.business.responses.*;
import com.personalApp.personal_service.entities.concretes.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class EmployeeMapper {
    @Mapping(source = "companyId", target = "company.id")
    @Mapping(source = "departmentId", target = "department.id")
    public abstract Employee toEntity(CreateEmployeeRequest dto);

    @Mapping(source = "companyId", target = "company.id")
    @Mapping(source = "departmentId", target = "department.id")
    public abstract void updateEmployee(UpdateEmployeeRequest updateEmployeeRequest,
                                        @MappingTarget Employee employee);

    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "company.name", target = "companyName")
    public abstract FindEmployeesByIdentityNumber toFindEmployeesByIdentityNumber(Employee employee);

    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "company.name", target = "companyName")
    public abstract FindEmployeesByFirstNameAndLastName toFindEmployeesByFirstNameAndLastName(Employee employee);

    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "company.name", target = "companyName")
    public abstract FindEmployeesByFirstName toFindEmployeesByFirstName(Employee employee);

    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "company.name", target = "companyName")
    public abstract FindEmployeesByLastName toFindEmployeesByLastName(Employee employee);

    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "company.name", target = "companyName")
    public abstract FindEmployeesByCompanyIdAndDepartmentId toFindEmployeesByCompanyIdAndDepartmentId(Employee employee);

    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "company.name", target = "companyName")
    public abstract GetAllEmployeeResponse toGetAllEmployeeResponse(Employee employee);

    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "company.name", target = "companyName")
    public abstract GetByIdEmployeeResponse toGetByIdEmployeeResponse(Employee employee);

    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "company.name", target = "companyName")
    public abstract FindEmployeesByDepartmentResponse toFindEmployeesByDepartmentResponse(Employee employee);

    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "company.name", target = "companyName")
    public abstract FindEmployeesByCompanyResponse toFindEmployeesByCompanyResponse(Employee employee);
}