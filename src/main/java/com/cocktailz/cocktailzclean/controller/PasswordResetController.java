package com.cocktailz.cocktailzclean.controller;

import com.cocktailz.cocktailzclean.dto.ResetPasswordDto;
import com.cocktailz.cocktailzclean.dto.ResetPasswordRequest;
import com.cocktailz.cocktailzclean.entity.PasswordResetToken;
import com.cocktailz.cocktailzclean.entity.User;
import com.cocktailz.cocktailzclean.repository.PasswordResetTokenRepository;
import com.cocktailz.cocktailzclean.repository.UserRepository;
import com.cocktailz.cocktailzclean.service.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetController(UserRepository userRepository,
                                   PasswordResetTokenRepository tokenRepository,
                                   EmailService emailService,
                                   PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    // 🔹 Step 1: User requests a password reset
    @PostMapping("/reset-password-request")
    public ResponseEntity<String> requestPasswordReset(@RequestBody ResetPasswordRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Email not found");
        }

        User user = userOpt.get();

        // Check if a token already exists for this user
        Optional<PasswordResetToken> existingToken = tokenRepository.findByUser(user);

        PasswordResetToken resetToken;
        if (existingToken.isPresent()) {
            // Update existing token
            resetToken = existingToken.get();
            resetToken.setToken(UUID.randomUUID().toString());
            resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        } else {
            // Create new token
            resetToken = new PasswordResetToken();
            resetToken.setUser(user);
            resetToken.setToken(UUID.randomUUID().toString());
            resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        }

        tokenRepository.save(resetToken);

        // Build reset link
        String resetLink = "http://localhost:5173/PasswordResetPage?token=" + resetToken.getToken();

        // Send email
        emailService.sendEmail(
                user.getEmail(),
                "Wachtwoord reset",
                "Klik op deze link om je wachtwoord te resetten: " + resetLink
        );

        return ResponseEntity.ok("Reset link sent to email");
    }

    // 🔹 Step 2: User sets new password
    @Transactional
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDto dto) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(dto.getToken());

        if (tokenOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid token");
        }

        PasswordResetToken resetToken = tokenOpt.get();

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        // Invalidate token
        tokenRepository.delete(resetToken);

        return ResponseEntity.ok("Password successfully reset");
    }
}
