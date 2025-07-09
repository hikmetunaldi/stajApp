package com.personalApp.personal_service.helpers.exceptions;

public class DuplicateIdentityNumberException extends RuntimeException{
    public DuplicateIdentityNumberException(String identityNumber){
        super("Identity number already exists: " +identityNumber);
    }
}
