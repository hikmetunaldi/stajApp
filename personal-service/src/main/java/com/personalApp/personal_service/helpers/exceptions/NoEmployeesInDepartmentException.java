package com.personalApp.personal_service.helpers.exceptions;

public class NoEmployeesInDepartmentException extends RuntimeException {
    public NoEmployeesInDepartmentException(int departmentId) {
        super("No employees in department with id: " + departmentId);
    }
}
