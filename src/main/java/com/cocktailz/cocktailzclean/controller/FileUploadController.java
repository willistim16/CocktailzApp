package com.cocktailz.cocktailzclean.controller;

import com.cocktailz.cocktailzclean.Entity.User;
import com.cocktailz.cocktailzclean.Entity.UserProfile;
import com.cocktailz.cocktailzclean.repository.UserProfileRepository;
import com.cocktailz.cocktailzclean.repository.UserRepository;
import com.cocktailz.cocktailzclean.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileStorageService fileStorageService; // interface!
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadProfileImage(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user
    ) throws IOException {
        String filename = fileStorageService.storeFile(file);

        UserProfile profile = user.getUserProfile();
        if (profile == null) {
            profile = new UserProfile();
            profile.setUser(user);
        }

        profile.setProfileImagePath(filename);
        userProfileRepository.save(profile);

        return ResponseEntity.ok("Bestand succesvol geüpload als: " + filename);
    }
}
