package com.personalApp.personal_service.helpers.exceptions;

import com.personalApp.personal_service.entities.concretes.Company;
import com.personalApp.personal_service.entities.concretes.Department;

public class ValidationHelper {
    public static void validateDepartmentBelongsToCompany(Department department, Company company){
        if(department.getCompany().getId() != company.getId()){
            throw new IllegalArgumentException(("Department is not part of the selected company"));
        }
    }
}
