package com.cocktailz.cocktailzclean.repository;

import com.cocktailz.cocktailzclean.Entity.Rating;
import com.cocktailz.cocktailzclean.Entity.Cocktail;
import com.cocktailz.cocktailzclean.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByCocktail(Cocktail cocktail);

    Optional<Rating> findByUserAndCocktail(User user, Cocktail cocktail);
}
