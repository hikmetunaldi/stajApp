package com.personalApp.personal_service.security.controller;

import com.personalApp.personal_service.security.dto.LoginRequest;
import com.personalApp.personal_service.security.dto.LoginResponse;
import com.personalApp.personal_service.security.model.User;
import com.personalApp.personal_service.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String credentials = loginRequest.getUsername() + ":" + loginRequest.getPassword();
            String token = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());

            LoginResponse response = new LoginResponse();
            response.setToken(token);
            response.setUsername(user.getUsername());
            response.setRole(user.getRole().replace("ROLE_", ""));
            response.setMessage("Login successful");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            LoginResponse errorResponse = new LoginResponse();
            errorResponse.setMessage("Invalid credentials");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}