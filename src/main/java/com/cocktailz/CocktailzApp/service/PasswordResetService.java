package com.cocktailz.CocktailzApp.service;

import com.cocktailz.CocktailzApp.entity.PasswordResetToken;
import com.cocktailz.CocktailzApp.entity.User;
import com.cocktailz.CocktailzApp.repository.PasswordResetTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;


    public PasswordResetService(PasswordResetTokenRepository passwordResetTokenRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;

    }

    public void createAndSendResetToken(User user) {
        String newToken = UUID.randomUUID().toString();

        passwordResetTokenRepository.findByUser(user).ifPresentOrElse(
                tokenToUpdate -> {
                    tokenToUpdate.setToken(newToken);
                    tokenToUpdate.setExpiryDate(LocalDateTime.now().plusHours(1));
                    passwordResetTokenRepository.save(tokenToUpdate);
                },
                () -> {
                    PasswordResetToken newPasswordResetToken = new PasswordResetToken();
                    newPasswordResetToken.setUser(user);
                    newPasswordResetToken.setToken(newToken);
                    newPasswordResetToken.setExpiryDate(LocalDateTime.now().plusHours(1));
                    passwordResetTokenRepository.save(newPasswordResetToken);
                }
        );

        System.out.println("Password reset token for user " + user.getUsername() + ": " + newToken);
    }

}
