package com.personalApp.personal_service.helpers.exceptions;

public class EmployeeNotFoundByLastNameException extends RuntimeException {
    public EmployeeNotFoundByLastNameException(String lastName) {
      super("Employee with last name: " + lastName + " not found");
    }
}
