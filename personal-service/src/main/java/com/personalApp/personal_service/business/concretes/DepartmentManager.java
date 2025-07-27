package com.personalApp.personal_service.business.concretes;

import com.personalApp.personal_service.business.abstracts.DepartmentService;
import com.personalApp.personal_service.business.requests.CreateDepartmentRequest;
import com.personalApp.personal_service.business.requests.UpdateDepartmentRequest;
import com.personalApp.personal_service.business.responses.GetAllDepartmentResponse;
import com.personalApp.personal_service.business.responses.GetByIdDepartmantResponse;
import com.personalApp.personal_service.core.utilities.mappers.DepartmentMapper;
import com.personalApp.personal_service.core.utilities.mappers.ModelMapperService;
import com.personalApp.personal_service.dataAccess.abstracts.CompanyRepository;
import com.personalApp.personal_service.dataAccess.abstracts.DepartmentRepository;
import com.personalApp.personal_service.entities.concretes.Department;
import com.personalApp.personal_service.helpers.exceptions.DepartmentNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;


@Service
@AllArgsConstructor
public class DepartmentManager implements DepartmentService {

    private final CompanyRepository companyRepository;
    private final ModelMapperService modelMapperService;
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public List<GetAllDepartmentResponse> getAll() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream().map(Department -> modelMapperService.forResponse()
                        .map(Department, GetAllDepartmentResponse.class))
                .sorted(Comparator.comparing(GetAllDepartmentResponse::getId)).toList();
    }


    public GetByIdDepartmantResponse getById(int id) {
        Department department = departmentRepository.findById(id).orElseThrow(()->new DepartmentNotFoundException(id));

        return modelMapperService.forResponse().map(department, GetByIdDepartmantResponse.class);
    }

    @Override
    public void add(CreateDepartmentRequest createDepartmentRequest) {
        companyRepository.findById(createDepartmentRequest.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));
        var department = departmentMapper.toEntity(createDepartmentRequest);

        departmentRepository.save(department);
    }

    public void update(UpdateDepartmentRequest updateDepartmentRequest) {
        Department department = modelMapperService.forRequest()
                .map(updateDepartmentRequest, Department.class);
        departmentRepository.save(department);
    }

    public void delete(int id) {

        departmentRepository.deleteById(id);
    }

    @Override
    public Department getDepartmentById(int id) {
        return departmentRepository.findById(id).orElseThrow(()->new DepartmentNotFoundException(id));
    }

}
