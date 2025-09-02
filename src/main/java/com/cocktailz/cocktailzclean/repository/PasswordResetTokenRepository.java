package com.cocktailz.cocktailzclean.repository;

import com.cocktailz.cocktailzclean.entity.PasswordResetToken;
import com.cocktailz.cocktailzclean.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

    public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
        Optional<PasswordResetToken> findByToken(String token);
        Optional<PasswordResetToken> findByUser(User user);

    }


