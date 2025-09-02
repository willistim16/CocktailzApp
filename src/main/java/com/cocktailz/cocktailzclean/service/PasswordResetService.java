package com.cocktailz.cocktailzclean.service;

import com.cocktailz.cocktailzclean.entity.PasswordResetToken;
import com.cocktailz.cocktailzclean.entity.User;
import com.cocktailz.cocktailzclean.repository.PasswordResetTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    public PasswordResetService(PasswordResetTokenRepository passwordResetTokenRepository,
                                EmailService emailService) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
    }

    public void createAndSendResetToken(User user) {
        String newToken = UUID.randomUUID().toString();

        Optional<PasswordResetToken> existingToken = passwordResetTokenRepository.findByUser(user);

        if (existingToken.isPresent()) {
            PasswordResetToken tokenToUpdate = existingToken.get();
            tokenToUpdate.setToken(newToken);
            tokenToUpdate.setExpiryDate(LocalDateTime.now().plusHours(1));
            passwordResetTokenRepository.save(tokenToUpdate);
        } else {
            PasswordResetToken newPasswordResetToken = new PasswordResetToken();
            newPasswordResetToken.setUser(user);
            newPasswordResetToken.setToken(newToken);
            newPasswordResetToken.setExpiryDate(LocalDateTime.now().plusHours(1));
            passwordResetTokenRepository.save(newPasswordResetToken);
        }

        // Token mailen via EmailService
        emailService.sendEmail(
                user.getEmail(),
                "Password Reset",
                "Use this token to reset your password: " + newToken
        );
    }
}
