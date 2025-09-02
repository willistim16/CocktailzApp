package com.cocktailz.cocktailzclean.controller;

import com.cocktailz.cocktailzclean.entity.User;
import com.cocktailz.cocktailzclean.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileDownloadController {

    private final FileStorageService fileStorageService;

    @GetMapping("/{filename:.+}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Resource> getFile(
            @PathVariable String filename,
            @AuthenticationPrincipal User user
    ) throws MalformedURLException {

        // ✅ Controleer of de gevraagde file overeenkomt met de ingelogde gebruiker
        if (!filename.equals(user.getProfileImagePath())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Path filePath = fileStorageService.getFilePath(filename);
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        String contentType = "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .body(resource);
    }
}
