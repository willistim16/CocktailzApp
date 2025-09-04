package com.cocktailz.CocktailzApp.service;

import com.cocktailz.CocktailzApp.dto.RatingDto;
import com.cocktailz.CocktailzApp.entity.Cocktail;
import com.cocktailz.CocktailzApp.entity.Rating;
import com.cocktailz.CocktailzApp.entity.User;

import java.util.List;
import java.util.Optional;

public interface RatingService {

    List<RatingDto> getRatings(User user);

    Rating rate(User user, RatingDto dto);

    Optional<Rating> findByUserAndCocktail(User user, Cocktail cocktail);

    List<Rating> findByCocktail(Cocktail cocktail);
}
