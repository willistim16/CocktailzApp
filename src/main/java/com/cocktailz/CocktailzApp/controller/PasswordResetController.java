package com.cocktailz.CocktailzApp.controller;

import com.cocktailz.CocktailzApp.dto.ResetPasswordDto;
import com.cocktailz.CocktailzApp.dto.ResetPasswordRequest;
import com.cocktailz.CocktailzApp.entity.PasswordResetToken;
import com.cocktailz.CocktailzApp.entity.User;
import com.cocktailz.CocktailzApp.repository.PasswordResetTokenRepository;
import com.cocktailz.CocktailzApp.repository.UserRepository;
import com.cocktailz.CocktailzApp.service.EmailService;
import com.cocktailz.CocktailzApp.service.PasswordResetService;
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
    private final PasswordResetService passwordResetService;

    public PasswordResetController(UserRepository userRepository,
                                   PasswordResetTokenRepository tokenRepository,
                                   EmailService emailService,
                                   PasswordEncoder passwordEncoder,
                                   PasswordResetService passwordResetService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetService = passwordResetService;
    }

    /**
     * Generate a reset token without email (for testing users without email).
     */
    @GetMapping("/force-reset/{username}")
    public ResponseEntity<String> forceReset(@PathVariable String username) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    passwordResetService.createAndSendResetToken(user);
                    return ResponseEntity.ok(
                            "Reset token generated for user: " + username + " (check console for token)"
                    );
                })
                .orElse(ResponseEntity.badRequest()
                        .body("User not found: " + username));
    }

    /**
     * Request a password reset by email (normal flow).
     */
    @PostMapping("/reset-password-request")
    public ResponseEntity<String> requestPasswordReset(@RequestBody ResetPasswordRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Email not found");
        }

        User user = userOpt.get();

        Optional<PasswordResetToken> existingToken = tokenRepository.findByUser(user);
        PasswordResetToken resetToken;

        if (existingToken.isPresent()) {
            resetToken = existingToken.get();
            resetToken.setToken(UUID.randomUUID().toString());
            resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        } else {
            resetToken = new PasswordResetToken();
            resetToken.setUser(user);
            resetToken.setToken(UUID.randomUUID().toString());
            resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        }

        tokenRepository.save(resetToken);

        String resetLink = "http://localhost:5173/PasswordResetPage?token=" + resetToken.getToken();

        emailService.sendEmail(
                user.getEmail(),
                "Wachtwoord reset",
                "Klik op deze link om je wachtwoord te resetten: " + resetLink
        );

        return ResponseEntity.ok("Reset link sent to email");
    }

    /**
     * Perform the actual password reset using a valid token.
     */
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

        tokenRepository.delete(resetToken);

        return ResponseEntity.ok("Password successfully reset");
    }
}
