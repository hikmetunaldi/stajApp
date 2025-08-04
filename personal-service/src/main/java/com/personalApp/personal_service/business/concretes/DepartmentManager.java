package com.personalApp.personal_service.business.concretes;

import com.personalApp.personal_service.business.abstracts.DepartmentService;
import com.personalApp.personal_service.business.requests.CreateDepartmentRequest;
import com.personalApp.personal_service.business.requests.UpdateDepartmentRequest;
import com.personalApp.personal_service.business.responses.GetAllDepartmentResponse;
import com.personalApp.personal_service.business.responses.GetByIdDepartmentResponse;
import com.personalApp.personal_service.core.utilities.mappers.DepartmentMapper;
import com.personalApp.personal_service.dataAccess.abstracts.CompanyRepository;
import com.personalApp.personal_service.dataAccess.abstracts.DepartmentRepository;
import com.personalApp.personal_service.entities.concretes.Department;
import com.personalApp.personal_service.helpers.exceptions.CompanyNotFoundException;
import com.personalApp.personal_service.helpers.exceptions.DepartmentNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class DepartmentManager implements DepartmentService {

    private final CompanyRepository companyRepository;
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public List<GetAllDepartmentResponse> getAll() {
        List<Department> departments = departmentRepository.findAll();

        return departments.stream()
                .map(departmentMapper::toGetAllDepartmentResponse)
                .sorted(Comparator.comparing(GetAllDepartmentResponse::getId))
                .toList();
    }

    public GetByIdDepartmentResponse getById(int id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));

        return departmentMapper.toGetByIdDepartmentResponse(department);
    }

    @Override
    public void add(CreateDepartmentRequest createDepartmentRequest) {
        var company = companyRepository.findById(createDepartmentRequest.getCompanyId())
                .orElseThrow(() -> new CompanyNotFoundException(createDepartmentRequest.getCompanyId()));

        var department = departmentMapper.toEntity(createDepartmentRequest);
        department.setCompany(company);

        departmentRepository.save(department);
    }

    public void update(UpdateDepartmentRequest updateDepartmentRequest) {
        Department existingDepartment = departmentRepository.findById(updateDepartmentRequest.getId())
                .orElseThrow(() -> new DepartmentNotFoundException(updateDepartmentRequest.getId()));

        departmentMapper.updateDepartment(updateDepartmentRequest, existingDepartment);
        departmentRepository.save(existingDepartment);
    }

    public void delete(int id) {
        departmentRepository.deleteById(id);
    }
}