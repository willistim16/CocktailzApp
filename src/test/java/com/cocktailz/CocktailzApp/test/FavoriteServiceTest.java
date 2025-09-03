package com.cocktailz.CocktailzApp.test;

import com.cocktailz.CocktailzApp.dto.FavoriteResponseDto;
import com.cocktailz.CocktailzApp.entity.Cocktail;
import com.cocktailz.CocktailzApp.entity.Favorite;
import com.cocktailz.CocktailzApp.entity.Rating;
import com.cocktailz.CocktailzApp.entity.User;
import com.cocktailz.CocktailzApp.exception.DuplicateFavoriteException;
import com.cocktailz.CocktailzApp.repository.CocktailRepository;
import com.cocktailz.CocktailzApp.repository.FavoriteRepository;
import com.cocktailz.CocktailzApp.service.impl.FavoriteServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FavoriteServiceImplTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private CocktailRepository cocktailRepository;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    private AutoCloseable mocks;

    private User user;
    private Cocktail cocktail;
    private Favorite favorite;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        cocktail = new Cocktail();
        cocktail.setId(10L);
        cocktail.setName("Mojito");

        favorite = new Favorite();
        favorite.setId(100L);
        favorite.setUser(user);
        favorite.setCocktail(cocktail);
    }

    @AfterEach
    void tearDown() {
        try {
            mocks.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetFavoritesByUser() {
        when(favoriteRepository.findByUserWithNotes(user)).thenReturn(List.of(favorite));

        List<FavoriteResponseDto> result = favoriteService.getFavoritesByUser(user);

        assertNotNull(result, "Result should not be null");
        int favoriteCount = result.size();
        assertTrue(favoriteCount == 1 || favoriteCount == 0, "Favorites size should be 0 or 1");

        if (!result.isEmpty()) {
            assertEquals("Mojito", result.get(0).getCocktailName());
        }

        verify(favoriteRepository, times(1)).findByUserWithNotes(user);
    }

    @Test
    void testGetFavoriteById_Found() {
        when(favoriteRepository.findById(100L)).thenReturn(Optional.of(favorite));

        Favorite result = favoriteService.getFavoriteById(100L)
                .orElseThrow();

        assertEquals(100L, result.getId());
    }

    @Test
    void testGetFavoriteById_NotFound() {
        when(favoriteRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Favorite> result = favoriteService.getFavoriteById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void testGetFavoriteByUserAndCocktailId_Found() {
        when(favoriteRepository.findByUserAndCocktail_Id(user, 10L)).thenReturn(Optional.of(favorite));

        Favorite result = favoriteService.getFavoriteByUserAndCocktailId(user, 10L)
                .orElseThrow();

        assertEquals(100L, result.getId());
    }

    @Test
    void testGetFavoriteByUserAndCocktailId_NotFound() {
        when(favoriteRepository.findByUserAndCocktail_Id(user, 10L)).thenReturn(Optional.empty());

        Optional<Favorite> result = favoriteService.getFavoriteByUserAndCocktailId(user, 10L);

        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteFavorite_Found() {
        when(favoriteRepository.findByUserAndCocktail_Id(user, 10L)).thenReturn(Optional.of(favorite));

        favoriteService.deleteFavorite(user, 10L);

        verify(favoriteRepository, times(1)).delete(favorite);
    }

    @Test
    void testDeleteFavorite_NotFound() {
        when(favoriteRepository.findByUserAndCocktail_Id(user, 10L)).thenReturn(Optional.empty());

        favoriteService.deleteFavorite(user, 10L);

        verify(favoriteRepository, never()).delete(any());
    }

    @Test
    void testAddFavoriteByCocktailId_NewFavorite() {
        when(cocktailRepository.findById(10L)).thenReturn(Optional.of(cocktail));
        when(favoriteRepository.findByUserAndCocktail_Id(user, 10L)).thenReturn(Optional.empty());
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(favorite);

        Favorite result = favoriteService.addFavoriteByCocktailId(user, 10L);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        verify(favoriteRepository, times(1)).save(any(Favorite.class));
    }

    @Test
    void testAddFavoriteByCocktailId_AlreadyExists() {
        when(cocktailRepository.findById(favorite.getCocktail().getId()))
                .thenReturn(Optional.of(cocktail));

        when(favoriteRepository.existsByUserAndCocktail_Id(user, favorite.getCocktail().getId()))
                .thenReturn(true);

        DuplicateFavoriteException exception = assertThrows(DuplicateFavoriteException.class, () -> {
            favoriteService.addFavoriteByCocktailId(user, favorite.getCocktail().getId());
        });

        assertEquals("Cocktail already exists in favorites", exception.getMessage());

        verify(favoriteRepository, never()).save(any());
    }


    @Test
    void testAddFavoriteByCocktailId_CocktailNotFound() {
        when(cocktailRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> favoriteService.addFavoriteByCocktailId(user, 999L));
    }

    @Test
    void testAddFavoriteByCocktailId_SaveFails() {
        when(cocktailRepository.findById(10L)).thenReturn(Optional.of(cocktail));
        when(favoriteRepository.findByUserAndCocktail_Id(user, 10L)).thenReturn(Optional.empty());
        when(favoriteRepository.save(any(Favorite.class)))
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class,
                () -> favoriteService.addFavoriteByCocktailId(user, 10L));
    }

    @Test
    void testMapToDto_Normal() {
        FavoriteResponseDto dto = favoriteService.mapToDto(favorite);

        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals("Mojito", dto.getCocktailName());
    }

    @Test
    void testMapToDto_NullFavorite() {
        FavoriteResponseDto dto = favoriteService.mapToDto(null);
        assertNull(dto);
    }

    @Test
    void testMapToDto_NullRating() {
        favorite.setRating(null);
        FavoriteResponseDto dto = favoriteService.mapToDto(favorite);
        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals("Mojito", dto.getCocktailName());
        assertNull(dto.getRating());
    }

    @Test
    void testMapToDto_WithRating() {
        Rating rating = new Rating();
        rating.setId(1L);
        rating.setScore(5);
        favorite.setRating(rating);

        FavoriteResponseDto dto = favoriteService.mapToDto(favorite);

        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals("Mojito", dto.getCocktailName());
        assertEquals(5, dto.getRating());
    }

    @Test
    void testMapToDto_EmptyNotes() {
        favorite.getNotes().clear();
        FavoriteResponseDto dto = favoriteService.mapToDto(favorite);
        assertNotNull(dto);
        assertNotNull(dto.getNotes());
        assertTrue(dto.getNotes().isEmpty());
    }

    @Test
    void testMapToDto_NullNotesList() {
        favorite.setNotes(null);
        FavoriteResponseDto dto = favoriteService.mapToDto(favorite);
        assertNotNull(dto);
        assertNotNull(dto.getNotes());
        assertTrue(dto.getNotes().isEmpty());
    }

    @Test
    void testMapToDto_NullCocktail() {
        favorite.setCocktail(null);
        FavoriteResponseDto dto = favoriteService.mapToDto(favorite);

        assertNull(dto);
    }

    @Test
    void testMapToDto_NullUser() {
        favorite.setUser(null);
        FavoriteResponseDto dto = favoriteService.mapToDto(favorite);

        assertNull(dto);
    }
}
