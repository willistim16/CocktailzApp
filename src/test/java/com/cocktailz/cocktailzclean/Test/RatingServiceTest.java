package com.cocktailz.cocktailzclean.Test;

import com.cocktailz.cocktailzclean.entity.Cocktail;
import com.cocktailz.cocktailzclean.entity.Rating;
import com.cocktailz.cocktailzclean.entity.User;
import com.cocktailz.cocktailzclean.service.RatingService;
import com.cocktailz.cocktailzclean.repository.RatingRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingService ratingService;

    public RatingServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindRatingByUserAndCocktail() {
        User user = new User();
        user.setId(1L);

        Cocktail cocktail = new Cocktail();
        cocktail.setId(5L);

        Rating rating = new Rating();
        rating.setUser(user);
        rating.setCocktail(cocktail);
        rating.setScore(4);

        when(ratingRepository.findByUserAndCocktail(user, cocktail))
                .thenReturn(Optional.of(rating));

        Optional<Rating> result = ratingService.findByUserAndCocktail(user, cocktail);

        assertTrue(result.isPresent());
        assertEquals(4, result.get().getScore());
    }
}


