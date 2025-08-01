package com.cocktailz.cocktailzclean.repository;

import com.cocktailz.cocktailzclean.entity.Cocktail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CocktailRepository extends JpaRepository<Cocktail, Long> {

    List<Cocktail> findByAlcoholic(String alcoholic);

    List<Cocktail> findByNameContainingIgnoreCase(String name);
}
