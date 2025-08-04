package com.cocktailz.cocktailzclean.controller;

import com.cocktailz.cocktailzclean.entity.User;
import com.cocktailz.cocktailzclean.security.jwt.JwtUtil;
import com.cocktailz.cocktailzclean.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

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

    // DTO for register
    @Data
    public static class RegisterRequest {
        private String username;
        private String email;
        private String password;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    // DTO for login request
    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    // DTO for JWT response
    @Data
    @AllArgsConstructor
    public static class JwtResponse {
        private String token;
    }
}
