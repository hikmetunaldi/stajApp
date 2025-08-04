package com.personalApp.personal_service.helpers.exceptions;

public class EmployeeNotFoundByFirstNameException extends RuntimeException {
    public EmployeeNotFoundByFirstNameException(String firstName){
        super("Employee with first name: " + firstName + " not found");
    }
}
