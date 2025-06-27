package com.personalApp.personal_service.business.abstracts;

import com.personalApp.personal_service.business.requests.CreateDepartmantRequest;
import com.personalApp.personal_service.business.requests.UpdateDepartmentRequest;
import com.personalApp.personal_service.business.responses.GetAllDepartmantResponse;
import com.personalApp.personal_service.business.responses.GetByIdDepartmantResponse;
import com.personalApp.personal_service.entities.concretes.Department;

import java.util.List;

public interface DepartmentService {

    List<GetAllDepartmantResponse> getAll();
    GetByIdDepartmantResponse getById(int id);
    void add(CreateDepartmantRequest createDepartmantRequest);
    void update(UpdateDepartmentRequest updateDepartmentRequest);
    void delete (int id);
    Department getDepartmentById(int id);
}
