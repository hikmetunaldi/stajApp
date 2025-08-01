package com.personalApp.personal_service.helpers.exceptions;

public class NoEmployeesInCompanyException extends RuntimeException {
    public NoEmployeesInCompanyException(int companyId) {

        super("No employees in company with id: " + companyId);
    }
}
