package com.cocktailz.cocktailzclean.repository;

import com.cocktailz.cocktailzclean.entity.Cocktail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CocktailRepository extends JpaRepository<Cocktail, Long> {

    List<Cocktail> findByAlcoholic(Boolean alcoholic);  // Changed param to Boolean

    List<Cocktail> findByNameContainingIgnoreCase(String name);

    Optional<Cocktail> findByName(String name);

    @Query(value = "SELECT * FROM cocktail ORDER BY RANDOM() LIMIT :count", nativeQuery = true)
    List<Cocktail> findRandomCocktails(@Param("count") int count);
}
