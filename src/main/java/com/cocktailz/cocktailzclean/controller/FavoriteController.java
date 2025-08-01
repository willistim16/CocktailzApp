package com.cocktailz.cocktailzclean.controller;

import com.cocktailz.cocktailzclean.entity.User;
import com.cocktailz.cocktailzclean.dto.FavoriteRequest;
import com.cocktailz.cocktailzclean.dto.NoteRequest;
import com.cocktailz.cocktailzclean.dto.RatingRequest;
import com.cocktailz.cocktailzclean.service.FavoriteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addFavorite(@AuthenticationPrincipal User user,
                                         @Valid @RequestBody FavoriteRequest request) {
        return ResponseEntity.ok(favoriteService.addFavorite(user, request));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getFavorites(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(favoriteService.getFavorites(user));
    }

    @DeleteMapping("/{cocktailId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> removeFavorite(@AuthenticationPrincipal User user,
                                            @PathVariable Long cocktailId) {
        favoriteService.removeFavorite(user, cocktailId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{cocktailId}/note")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addOrUpdateNote(@AuthenticationPrincipal User user,
                                             @PathVariable Long cocktailId,
                                             @Valid @RequestBody NoteRequest request) {
        return ResponseEntity.ok(favoriteService.addOrUpdateNote(user, cocktailId, request));
    }

    @PostMapping("/{cocktailId}/rating")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addOrUpdateRating(@AuthenticationPrincipal User user,
                                               @PathVariable Long cocktailId,
                                               @Valid @RequestBody RatingRequest request) {
        return ResponseEntity.ok(favoriteService.addOrUpdateRating(user, cocktailId, request));
    }
}
