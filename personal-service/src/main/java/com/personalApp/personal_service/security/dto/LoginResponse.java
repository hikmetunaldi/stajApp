package com.personalApp.personal_service.security.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String username;
    private String role;
    private String message;
}