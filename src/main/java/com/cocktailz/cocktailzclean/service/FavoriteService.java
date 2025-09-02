package com.cocktailz.cocktailzclean.service;

import com.cocktailz.cocktailzclean.dto.FavoriteResponseDto;
import com.cocktailz.cocktailzclean.entity.Favorite;
import com.cocktailz.cocktailzclean.entity.User;

import java.util.List;
import java.util.Optional;

public interface FavoriteService {
    List<FavoriteResponseDto> getFavoritesByUser(User user);
    Optional<Favorite> getFavoriteById(Long id);
    Optional<Favorite> getFavoriteByUserAndCocktailId(User user, Long cocktailId);
    void deleteFavorite(User user, Long cocktailId);
    Favorite addFavoriteByCocktailId(User user, Long cocktailId);

    // Add this for DTO mapping
    FavoriteResponseDto mapToDto(Favorite favorite);
}
