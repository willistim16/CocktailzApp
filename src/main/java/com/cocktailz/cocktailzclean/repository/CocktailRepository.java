package com.cocktailz.cocktailzclean.repository;

import com.cocktailz.cocktailzclean.Entity.Cocktail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CocktailRepository extends JpaRepository<Cocktail, Long> {

    List<Cocktail> findByAlcoholic(boolean alcoholic);

    List<Cocktail> findByNameContainingIgnoreCase(String name);

    Optional<Cocktail> findByName(String name);

}
