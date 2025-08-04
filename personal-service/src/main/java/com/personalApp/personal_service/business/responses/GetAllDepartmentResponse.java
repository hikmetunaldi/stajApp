package com.personalApp.personal_service.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllDepartmentResponse {
    private Integer id;
    private String name;
    private String companyName;
}
