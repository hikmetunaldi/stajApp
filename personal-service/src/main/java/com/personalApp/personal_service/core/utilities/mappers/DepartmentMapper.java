package com.personalApp.personal_service.core.utilities.mappers;

import com.personalApp.personal_service.business.requests.CreateDepartmentRequest;
import com.personalApp.personal_service.business.requests.UpdateDepartmentRequest;
import com.personalApp.personal_service.business.responses.GetAllDepartmentResponse;
import com.personalApp.personal_service.business.responses.GetByIdDepartmentResponse;
import com.personalApp.personal_service.entities.concretes.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class DepartmentMapper {

    @Mapping(source = "companyId", target = "company.id")
    public abstract Department toEntity(CreateDepartmentRequest dto);

    @Mapping(source = "companyId", target = "company.id")
    public abstract void updateDepartment(UpdateDepartmentRequest updateDepartmentRequest,
                                          @MappingTarget Department department);

    @Mapping(source = "company.name", target = "companyName")
    public abstract GetAllDepartmentResponse toGetAllDepartmentResponse(Department department);

    @Mapping(source = "company.name", target = "companyName")
    public abstract GetByIdDepartmentResponse toGetByIdDepartmentResponse(Department department);
}