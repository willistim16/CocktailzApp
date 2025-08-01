package com.cocktailz.cocktailzclean.service;

import com.cocktailz.cocktailzclean.entity.*;
import com.cocktailz.cocktailzclean.dto.RatingDto;
import com.cocktailz.cocktailzclean.repository.FavoriteRepository;
import com.cocktailz.cocktailzclean.repository.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    private final FavoriteRepository favoriteRepository;
    private final RatingRepository ratingRepository;

    public RatingService(FavoriteRepository favoriteRepository, RatingRepository ratingRepository) {
        this.favoriteRepository = favoriteRepository;
        this.ratingRepository = ratingRepository;
    }

    public Rating rate(User user, RatingDto dto) {
        Favorite favorite = favoriteRepository.findById(dto.getFavoriteId())
                .filter(f -> f.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Favorite not found or not authorized"));

        Rating rating = favorite.getRating();
        if (rating == null) {
            rating = new Rating();
            rating.setUser(user);
            rating.setCocktail(favorite.getCocktail());
        }
        rating.setScore(dto.getScore());

        Rating saved = ratingRepository.save(rating);

        favorite.setRating(saved);
        favoriteRepository.save(favorite);

        return saved;
    }

    public Optional<Rating> findByUserAndCocktail(User user, Cocktail cocktail) {
        return ratingRepository.findByUserAndCocktail(user, cocktail);
    }

    public List<Rating> findByCocktail(Cocktail cocktail) {
        return ratingRepository.findByCocktail(cocktail);
    }
}
