package com.personalApp.personal_service.helpers.exceptions;

public class EmployeeIdentityNotFoundException extends RuntimeException {
    public EmployeeIdentityNotFoundException(String identityNumber) {
        super("User not found with identity number : " + identityNumber);
    }
}
