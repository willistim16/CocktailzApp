package com.cocktailz.cocktailzclean.repository;

import com.cocktailz.cocktailzclean.dto.FavoriteResponseDto;
import com.cocktailz.cocktailzclean.entity.Favorite;
import com.cocktailz.cocktailzclean.entity.Cocktail;
import com.cocktailz.cocktailzclean.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUser(User user);
    // Fetch a favorite with its notes eagerly
    @Query("SELECT f FROM Favorite f LEFT JOIN FETCH f.notes WHERE f.id = :id")
    Optional<Favorite> findByIdWithNotes(@Param("id") Long id);

    // Fetch all favorites for a user including notes
    @Query("SELECT DISTINCT f FROM Favorite f LEFT JOIN FETCH f.notes n LEFT JOIN FETCH f.rating WHERE f.user = :user")
    List<Favorite> findByUserWithNotes(@Param("user") User user);

    Optional<Favorite> findByUserAndCocktail_Id(User user, Long cocktailId); // Use DB ID

    void deleteByUserAndCocktail(User user, Cocktail cocktail);

    boolean existsByUserAndCocktail_Id(User user, Long cocktailId);

}
