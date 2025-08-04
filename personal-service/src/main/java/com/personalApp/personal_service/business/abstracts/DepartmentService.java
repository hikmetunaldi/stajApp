package com.personalApp.personal_service.business.abstracts;

import com.personalApp.personal_service.business.requests.CreateDepartmentRequest;
import com.personalApp.personal_service.business.requests.UpdateDepartmentRequest;
import com.personalApp.personal_service.business.responses.GetAllDepartmentResponse;
import com.personalApp.personal_service.business.responses.GetByIdDepartmentResponse;
import com.personalApp.personal_service.entities.concretes.Department;

import java.util.List;

public interface DepartmentService {

    List<GetAllDepartmentResponse> getAll();

    GetByIdDepartmentResponse getById(int id);

    void add(CreateDepartmentRequest createDepartmentRequest);

    void update(UpdateDepartmentRequest updateDepartmentRequest);

    void delete(int id);

}
