package com.cocktailz.cocktailzclean.Test;

import com.cocktailz.cocktailzclean.Entity.Cocktail;
import com.cocktailz.cocktailzclean.Entity.Favorite;
import com.cocktailz.cocktailzclean.Entity.User;
import com.cocktailz.cocktailzclean.repository.CocktailRepository;
import com.cocktailz.cocktailzclean.repository.FavoriteRepository;
import com.cocktailz.cocktailzclean.repository.NoteRepository;
import com.cocktailz.cocktailzclean.repository.RatingRepository;
import com.cocktailz.cocktailzclean.service.FavoriteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private CocktailRepository cocktailRepository;

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFavoritesByUser() {
        // Arrange
        User user = new User();
        user.setId(1L);

        Cocktail cocktail = new Cocktail();
        cocktail.setName("Mojito");

        Favorite fav = new Favorite();
        fav.setUser(user);
        fav.setCocktail(cocktail);

        when(favoriteRepository.findByUser(user)).thenReturn(List.of(fav));

        // Act
        List<Favorite> result = favoriteService.getFavorites(user);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Mojito", result.get(0).getCocktail().getName());
        verify(favoriteRepository, times(1)).findByUser(user);
    }
}
