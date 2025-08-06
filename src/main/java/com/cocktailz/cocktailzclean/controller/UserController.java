package com.cocktailz.cocktailzclean.controller;

import com.cocktailz.cocktailzclean.dto.UserResponse;
import com.cocktailz.cocktailzclean.entity.User;
import com.cocktailz.cocktailzclean.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    // Alleen voor admins: alle gebruikers ophalen
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers(); // nieuw service method
    }

    // Alleen voor admins: gebruiker ophalen op basis van username
    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    // Voor ingelogde gebruiker: eigen profielgegevens ophalen
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        UserResponse response = new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getUserProfile() != null ? user.getUserProfile().getProfileImagePath() : null
        );

        return ResponseEntity.ok(response);
    }
}
