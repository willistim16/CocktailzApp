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
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getUserProfile() != null ? user.getUserProfile().getProfileImagePath() : null))
                .toList();
    }

    @GetMapping("/{username}/info")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        UserResponse response = new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getUserProfile() != null ? user.getUserProfile().getProfileImagePath() : null
        );
        return ResponseEntity.ok(response);
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
