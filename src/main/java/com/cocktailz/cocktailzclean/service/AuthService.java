package com.cocktailz.cocktailzclean.service;

import com.cocktailz.cocktailzclean.dto.AuthResponse;
import com.cocktailz.cocktailzclean.entity.User;
import com.cocktailz.cocktailzclean.security.jwt.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthService(JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager,
                       UserService userService) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    public AuthResponse register(String username, String email, String password) {
        if (userService.existsByUsername(username)) {
            throw new IllegalArgumentException("Gebruikersnaam is al in gebruik.");
        }

        if (userService.existsByEmail(email)) {
            throw new IllegalArgumentException("E-mailadres is al in gebruik.");
        }

        // Pass raw password, encoding happens in UserServiceImpl
        User user = userService.registerUser(username, password, email);

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getUserProfile() != null ? user.getUserProfile().getProfileImagePath() : null
        );
    }

    public AuthResponse login(String username, String password) {
        try {
            // Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (Exception e) {
            throw new RuntimeException("Ongeldige gebruikersnaam of wachtwoord");
        }

        // Fetch user details after successful authentication
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Gebruiker niet gevonden");
        }

        String token = jwtUtil.generateToken(user);

        return new AuthResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getUserProfile() != null ? user.getUserProfile().getProfileImagePath() : null
        );
    }
}
