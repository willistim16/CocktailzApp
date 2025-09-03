package com.cocktailz.CocktailzApp.repository;

import com.cocktailz.CocktailzApp.entity.PasswordResetToken;
import com.cocktailz.CocktailzApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

    public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
        Optional<PasswordResetToken> findByToken(String token);
        Optional<PasswordResetToken> findByUser(User user);

    }


