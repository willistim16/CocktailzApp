package com.cocktailz.CocktailzApp.repository;

import com.cocktailz.CocktailzApp.entity.Favorite;
import com.cocktailz.CocktailzApp.entity.Cocktail;
import com.cocktailz.CocktailzApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUser(User user);
    @Query("SELECT f FROM Favorite f LEFT JOIN FETCH f.notes WHERE f.id = :id")

    Optional<Favorite> findByIdWithNotes(@Param("id") Long id);

    void deleteByUserAndCocktail(User user, Cocktail cocktail);

    @Query("SELECT DISTINCT f FROM Favorite f LEFT JOIN FETCH f.notes n LEFT JOIN FETCH f.rating WHERE f.user = :user")
    List<Favorite> findByUserWithNotes(@Param("user") User user);

    Optional<Favorite> findByUserAndCocktail_Id(User user, Long cocktailId);

    boolean existsByUserAndCocktail_Id(User user, Long cocktailId);

}
