package com.personalApp.personal_service.helpers.exceptions;

public class EmployeeNotFoundByCompanyAndDepartmentException extends RuntimeException {
    public EmployeeNotFoundByCompanyAndDepartmentException(int companyId, int departmentId) {
      super("Employee with company " + companyId + " and department " + departmentId + " not found");
    }
}
