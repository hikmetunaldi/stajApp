package com.personalApp.personal_service.helpers.exceptions;

public class EmployeeNotFoundByFirstNameAndLastNameException extends RuntimeException {
    public EmployeeNotFoundByFirstNameAndLastNameException(String firstName, String lastName) {
      super("Employee with name " + firstName + " " + lastName + " not found");
    }
}
