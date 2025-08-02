package com.cocktailz.cocktailzclean.controller;

import com.cocktailz.cocktailzclean.entity.User;
import com.cocktailz.cocktailzclean.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        try {
            User registeredUser = userService.registerUser(
                    request.getUsername(), request.getEmail(), request.getPassword()
            );
            return ResponseEntity.ok("User geregistreerd: " + registeredUser.getUsername());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DTO voor registeren
    @Data
    static class RegisterRequest {
        private String username;
        private String email;
        private String password;
    }
}
