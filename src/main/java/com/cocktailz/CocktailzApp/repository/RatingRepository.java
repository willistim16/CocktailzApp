package com.cocktailz.CocktailzApp.repository;

import com.cocktailz.CocktailzApp.entity.Rating;
import com.cocktailz.CocktailzApp.entity.Cocktail;
import com.cocktailz.CocktailzApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByCocktail(Cocktail cocktail);

    Optional<Rating> findByUserAndCocktail(User user, Cocktail cocktail);

    List<Rating> findByUser(User user);
}
