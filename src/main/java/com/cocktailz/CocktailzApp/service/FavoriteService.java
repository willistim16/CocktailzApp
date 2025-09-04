package com.cocktailz.CocktailzApp.service;

import com.cocktailz.CocktailzApp.dto.FavoriteResponseDto;
import com.cocktailz.CocktailzApp.entity.Favorite;
import com.cocktailz.CocktailzApp.entity.User;

import java.util.List;
import java.util.Optional;

public interface FavoriteService {

    List<FavoriteResponseDto> getFavoritesByUser(User user);

    Optional<Favorite> getFavoriteById(Long id);

    Optional<Favorite> getFavoriteByUserAndCocktailId(User user, Long cocktailId);

    void deleteFavorite(User user, Long cocktailId);

    Favorite addFavoriteByCocktailId(User user, Long cocktailId);

    FavoriteResponseDto mapToDto(Favorite favorite);
}
