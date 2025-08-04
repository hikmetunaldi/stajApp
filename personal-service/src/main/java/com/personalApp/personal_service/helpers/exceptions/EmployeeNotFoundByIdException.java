package com.personalApp.personal_service.helpers.exceptions;

public class EmployeeNotFoundByIdException extends RuntimeException {
    public EmployeeNotFoundByIdException(int employeeId) {
        super("Employee with id: " + employeeId + " not found");
    }
}
