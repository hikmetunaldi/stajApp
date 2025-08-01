package com.personalApp.personal_service.helpers.exceptions;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(int departmentId) {

        super("Department not found with id: " + departmentId);
    }
}
