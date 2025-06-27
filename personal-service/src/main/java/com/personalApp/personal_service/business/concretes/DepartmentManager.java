package com.personalApp.personal_service.business.concretes;

import com.personalApp.personal_service.business.abstracts.DepartmentService;
import com.personalApp.personal_service.business.requests.CreateDepartmantRequest;
import com.personalApp.personal_service.business.requests.UpdateDepartmentRequest;
import com.personalApp.personal_service.business.responses.GetAllDepartmantResponse;
import com.personalApp.personal_service.business.responses.GetByIdDepartmantResponse;
import com.personalApp.personal_service.core.utilities.mappers.ModelMapperService;
import com.personalApp.personal_service.dataAccess.abstracts.CompanyRepository;
import com.personalApp.personal_service.dataAccess.abstracts.DepartmentRepository;
import com.personalApp.personal_service.entities.concretes.Company;
import com.personalApp.personal_service.entities.concretes.Department;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class DepartmentManager implements DepartmentService {

    @Autowired
    private final CompanyRepository companyRepository;
    private ModelMapperService modelMapperService;
    private DepartmentRepository departmentRepository;


    @Override
    public List<GetAllDepartmantResponse> getAll(){
        List<Department> departments = departmentRepository.findAll();

        List<GetAllDepartmantResponse> responses = departments.stream().map(department -> {
            GetAllDepartmantResponse response = new GetAllDepartmantResponse();
            response.setId(department.getId());
            response.setName(department.getName());
            response.setCompanyName(department.getCompany().getName()); // Şu satır kritik!
            return response;
        }).collect(Collectors.toList());

        return responses;
    }


    public GetByIdDepartmantResponse getById(int id){
        Department department = departmentRepository.findById(id).orElseThrow();

        GetByIdDepartmantResponse getByIdDepartmantResponse = this.modelMapperService.forResponse().
                map(department,GetByIdDepartmantResponse.class);

        return getByIdDepartmantResponse;
    }

    @Override
    public void add(CreateDepartmantRequest createDepartmantRequest) {
        Department department = new Department();
        department.setName(createDepartmantRequest.getName());

        Company company = companyRepository.findById(createDepartmantRequest.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found!"));

        department.setCompany(company);

        departmentRepository.save(department);
        System.out.println("Departmant başarıyla eklendi: " + department.getName());
    }

    public void update(UpdateDepartmentRequest updateDepartmentRequest){
        Department department = this.modelMapperService.forRequest()
                .map(updateDepartmentRequest, Department.class);
        this.departmentRepository.save(department);
    }

    public void delete(int id){

        this.departmentRepository.deleteById(id);
    }

    @Override
    public Department getDepartmentById(int id) {
        return departmentRepository.findById(id).orElseThrow();
    }

}
