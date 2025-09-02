package com.cocktailz.cocktailzclean.controller;

import com.cocktailz.cocktailzclean.dto.FavoriteRequest;
import com.cocktailz.cocktailzclean.dto.FavoriteResponseDto;
import com.cocktailz.cocktailzclean.entity.Favorite;
import com.cocktailz.cocktailzclean.entity.User;
import com.cocktailz.cocktailzclean.service.FavoriteService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@PreAuthorize("isAuthenticated()")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public List<FavoriteResponseDto> getFavorites(@AuthenticationPrincipal User user) {
        return favoriteService.getFavoritesByUser(user);
    }

    @GetMapping("/{id}")
    public FavoriteResponseDto getFavorite(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Favorite favorite = favoriteService.getFavoriteById(id)
                .orElseThrow(() -> new RuntimeException("Favorite not found"));
        // Optioneel extra check: favoriet hoort bij deze user
        if (!favorite.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to favorite");
        }
        return favoriteService.mapToDto(favorite);
    }

    @DeleteMapping("/{cocktailId}")
    public void deleteFavorite(@AuthenticationPrincipal User user, @PathVariable Long cocktailId) {
        favoriteService.deleteFavorite(user, cocktailId);
    }

    @PostMapping
    public FavoriteResponseDto addFavorite(@AuthenticationPrincipal User user, @RequestBody FavoriteRequest request) {
        Favorite fav = favoriteService.addFavoriteByCocktailId(user, request.getCocktailId());
        return favoriteService.mapToDto(fav);
    }
}
