package com.personalApp.personal_service.helpers.exceptions;

public class EmployeeIdentityNotFoundException extends RuntimeException {
    public EmployeeIdentityNotFoundException(String identityNumber) {
        super("Employee not found with identity number : " + identityNumber);
    }
}
