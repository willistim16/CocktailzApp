package com.cocktailz.CocktailzApp.service;

import com.cocktailz.CocktailzApp.dto.AuthResponse;
import com.cocktailz.CocktailzApp.entity.PasswordResetToken;
import com.cocktailz.CocktailzApp.entity.User;
import com.cocktailz.CocktailzApp.repository.PasswordResetTokenRepository;
import com.cocktailz.CocktailzApp.security.jwt.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AuthService(JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager,
                       UserService userService,
                       PasswordResetTokenRepository tokenRepository,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public AuthResponse register(String username, String email, String password) {
        if (userService.existsByUsername(username)) {
            throw new IllegalArgumentException("Gebruikersnaam is al in gebruik.");
        }
        if (userService.existsByEmail(email)) {
            throw new IllegalArgumentException("E-mailadres is al in gebruik.");
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = userService.registerUser(username, email, encodedPassword);

        String jwt = jwtUtil.generateToken(user);
        return new AuthResponse(
                jwt,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getUserProfile() != null ? user.getUserProfile().getProfileImagePath() : null
        );
    }

    public AuthResponse login(String username, String password) {
        System.out.println("DEBUG: login attempt username=" + username + ", password=" + password);

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

        User user = userService.findByUsername(username);

        String jwt = jwtUtil.generateToken(user);
        return new AuthResponse(
                jwt,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getUserProfile() != null ? user.getUserProfile().getProfileImagePath() : null
        );
    }

    public void sendResetEmail(String email) {
        User user;
        try {
            user = userService.findByEmail(email);
        } catch (RuntimeException e) {
            return;
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        tokenRepository.save(resetToken);

        String resetLink = "http://frontend-url/reset-password?token=" + token;

        emailService.sendEmail(
                user.getEmail(),
                "Wachtwoord reset",
                "Klik op deze link om je wachtwoord te resetten: " + resetLink
        );
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Ongeldige of verlopen token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token is verlopen");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.updateUser(user);

        tokenRepository.delete(resetToken);
    }
}
