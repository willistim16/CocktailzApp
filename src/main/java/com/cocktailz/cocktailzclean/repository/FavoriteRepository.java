package com.cocktailz.cocktailzclean.repository;

import com.cocktailz.cocktailzclean.Entity.Favorite;
import com.cocktailz.cocktailzclean.Entity.User;
import com.cocktailz.cocktailzclean.Entity.Cocktail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUser(User user);

    Optional<Favorite> findByUserAndCocktail(User user, Cocktail cocktail);

    void deleteByUserAndCocktail(User user, Cocktail cocktail);
}
