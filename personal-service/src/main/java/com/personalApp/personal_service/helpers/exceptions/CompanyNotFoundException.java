package com.personalApp.personal_service.helpers.exceptions;

public class CompanyNotFoundException extends RuntimeException {
    public CompanyNotFoundException(int companyId) {

      super("Company not found with id:" + companyId);
    }
}
