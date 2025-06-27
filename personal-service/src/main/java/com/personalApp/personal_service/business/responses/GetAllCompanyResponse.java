package com.personalApp.personal_service.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllCompanyResponse {

    private int id;
    private String name;
}
