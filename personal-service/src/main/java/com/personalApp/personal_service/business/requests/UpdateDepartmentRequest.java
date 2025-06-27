package com.personalApp.personal_service.business.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDepartmentRequest {

    private int id;
    private String name;
    private int companyId;
}
