package com.personalApp.personal_service.business.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCompanyRequest {

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;
}
