package com.cocktailz.CocktailzApp.service.impl;

import com.cocktailz.CocktailzApp.dto.RatingDto;
import com.cocktailz.CocktailzApp.entity.*;
import com.cocktailz.CocktailzApp.repository.FavoriteRepository;
import com.cocktailz.CocktailzApp.repository.RatingRepository;
import com.cocktailz.CocktailzApp.service.RatingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RatingServiceImpl implements RatingService {

    private final FavoriteRepository favoriteRepository;
    private final RatingRepository ratingRepository;

    public RatingServiceImpl(FavoriteRepository favoriteRepository, RatingRepository ratingRepository) {
        this.favoriteRepository = favoriteRepository;
        this.ratingRepository = ratingRepository;
    }

    @Override
    public List<RatingDto> getRatings(User user) {
        return ratingRepository.findByUser(user).stream()
                .map(r -> new RatingDto(r.getCocktail().getId(), r.getScore()))
                .collect(Collectors.toList());
    }

    @Override
    public Rating rate(User user, RatingDto dto) {
        Favorite favorite = favoriteRepository.findByUserAndCocktail_Id(user, dto.getCocktailId())
                .orElseThrow(() -> new RuntimeException("Favorite not found or not authorized"));

        Rating rating = favorite.getRating();
        if (rating == null) {
            rating = Rating.builder()
                    .user(user)
                    .cocktail(favorite.getCocktail())
                    .score(dto.getScore())
                    .build();
        } else {
            rating.setScore(dto.getScore());
        }

        Rating saved = ratingRepository.save(rating);
        favorite.setRating(saved);
        favoriteRepository.save(favorite);

        return saved;
    }

    @Override
    public Optional<Rating> findByUserAndCocktail(User user, Cocktail cocktail) {
        return ratingRepository.findByUserAndCocktail(user, cocktail);
    }

    @Override
    public List<Rating> findByCocktail(Cocktail cocktail) {
        return ratingRepository.findByCocktail(cocktail);
    }
}
