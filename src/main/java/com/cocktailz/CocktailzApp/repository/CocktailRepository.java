package com.cocktailz.CocktailzApp.repository;

import com.cocktailz.CocktailzApp.entity.Cocktail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CocktailRepository extends JpaRepository<Cocktail, Long> {

    List<Cocktail> findByAlcoholic(Boolean alcoholic);

    List<Cocktail> findByNameContainingIgnoreCase(String name);

    Optional<Cocktail> findByName(String name);

    @Query("SELECT DISTINCT c.category FROM Cocktail c WHERE c.category IS NOT NULL")
    List<String> findDistinctCategories();

    @Query("SELECT DISTINCT c.glass FROM Cocktail c WHERE c.glass IS NOT NULL")
    List<String> findDistinctGlasses();

    @Query(value = "SELECT * FROM cocktail ORDER BY RANDOM() LIMIT :count", nativeQuery = true)
    List<Cocktail> findRandomCocktails(@Param("count") int count);

    @Query("SELECT c FROM Cocktail c WHERE LOWER(c.ingredient) LIKE LOWER(CONCAT('%', :ingredient, '%'))")
    List<Cocktail> findByIngredientContainingIgnoreCase(@Param("ingredient") String ingredient);

    List<Cocktail> findByAlcoholicAndCategoryAndGlass(Boolean alcoholic, String category, String glass);

    List<Cocktail> findByAlcoholicAndCategory(Boolean alcoholic, String category);

    List<Cocktail> findByAlcoholicAndGlass(Boolean alcoholic, String glass);

    List<Cocktail> findByCategoryAndGlass(String category, String glass);

    List<Cocktail> findByCategory(String category);

    List<Cocktail> findByGlass(String glass);
}
