package com.cocktailz.CocktailzApp.controller;

import com.cocktailz.CocktailzApp.dto.AuthResponse;
import com.cocktailz.CocktailzApp.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request.getUsername(), request.getEmail(), request.getPassword());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @Data
    public static class RegisterRequest {
        private String username;
        private String email;
        private String password;
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }
}
