package com.cocktailz.cocktailzclean.service;

import com.cocktailz.cocktailzclean.dto.RatingDto;
import com.cocktailz.cocktailzclean.entity.Cocktail;
import com.cocktailz.cocktailzclean.entity.Rating;
import com.cocktailz.cocktailzclean.entity.User;

import java.util.List;
import java.util.Optional;

public interface RatingService {

    List<RatingDto> getRatings(User user);

    Rating rate(User user, RatingDto dto);

    Optional<Rating> findByUserAndCocktail(User user, Cocktail cocktail);

    List<Rating> findByCocktail(Cocktail cocktail);
}
