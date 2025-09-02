package com.cocktailz.cocktailzclean.test;

import com.cocktailz.cocktailzclean.dto.RatingDto;
import com.cocktailz.cocktailzclean.entity.Cocktail;
import com.cocktailz.cocktailzclean.entity.Favorite;
import com.cocktailz.cocktailzclean.entity.Rating;
import com.cocktailz.cocktailzclean.entity.User;
import com.cocktailz.cocktailzclean.repository.FavoriteRepository;
import com.cocktailz.cocktailzclean.repository.RatingRepository;
import com.cocktailz.cocktailzclean.service.RatingService;
import com.cocktailz.cocktailzclean.service.impl.RatingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RatingServiceTest {

    @Mock private RatingRepository ratingRepository;
    @Mock private FavoriteRepository favoriteRepository;
    @InjectMocks private RatingServiceImpl ratingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindRatingByUserAndCocktail() {
        User user = new User(); user.setId(1L);
        Cocktail cocktail = new Cocktail(); cocktail.setId(5L);
        Rating rating = new Rating(); rating.setUser(user); rating.setCocktail(cocktail); rating.setScore(4);

        when(ratingRepository.findByUserAndCocktail(user, cocktail)).thenReturn(Optional.of(rating));

        Optional<Rating> result = ratingService.findByUserAndCocktail(user, cocktail);
        assertTrue(result.isPresent());
        assertEquals(4, result.get().getScore());
    }

    @Test
    void testRate_NewRating() {
        User user = new User(); user.setId(1L);
        Cocktail cocktail = new Cocktail(); cocktail.setId(5L);
        Favorite favorite = new Favorite(); favorite.setUser(user); favorite.setCocktail(cocktail);

        when(favoriteRepository.findByUserAndCocktail_Id(user, 5L)).thenReturn(Optional.of(favorite));
        when(ratingRepository.save(any(Rating.class))).thenAnswer(i -> i.getArgument(0));
        when(favoriteRepository.save(any(Favorite.class))).thenAnswer(i -> i.getArgument(0));

        RatingDto dto = new RatingDto(); dto.setCocktailId(5L); dto.setScore(5);
        Rating rating = ratingService.rate(user, dto);

        assertEquals(5, rating.getScore());
        verify(ratingRepository, times(1)).save(any(Rating.class));
        verify(favoriteRepository, times(1)).save(any(Favorite.class));
    }

    @Test
    void testGetRatings() {
        User user = new User(); user.setId(1L);
        Cocktail cocktail = new Cocktail(); cocktail.setId(5L);
        Rating rating = new Rating(); rating.setUser(user); rating.setCocktail(cocktail); rating.setScore(3);

        when(ratingRepository.findByUser(user)).thenReturn(List.of(rating));

        List<RatingDto> ratings = ratingService.getRatings(user);
        assertEquals(1, ratings.size());
        assertEquals(3, ratings.get(0).getScore());
    }
}
