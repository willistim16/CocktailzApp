package com.cocktailz.cocktailzclean.service;

import com.cocktailz.cocktailzclean.Entity.*;
import com.cocktailz.cocktailzclean.dto.FavoriteRequest;
import com.cocktailz.cocktailzclean.dto.NoteRequest;
import com.cocktailz.cocktailzclean.dto.RatingRequest;
import com.cocktailz.cocktailzclean.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepo;
    private final CocktailRepository cocktailRepo;
    private final NoteRepository noteRepo;
    private final RatingRepository ratingRepo;

    public FavoriteService(FavoriteRepository favoriteRepo,
                           CocktailRepository cocktailRepo,
                           NoteRepository noteRepo,
                           RatingRepository ratingRepo) {
        this.favoriteRepo = favoriteRepo;
        this.cocktailRepo = cocktailRepo;
        this.noteRepo = noteRepo;
        this.ratingRepo = ratingRepo;
    }

    /**
     * Retrieve all favorites for a given user.
     */
    public List<Favorite> getFavorites(User user) {
        return favoriteRepo.findByUser(user);
    }

    /**
     * Add a new favorite cocktail for the user.
     */
    @Transactional
    public Favorite addFavorite(User user, FavoriteRequest request) {
        Cocktail cocktail = cocktailRepo.findById(request.getCocktailId())
                .orElseThrow(() -> new RuntimeException("Cocktail not found with id: " + request.getCocktailId()));

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setCocktail(cocktail);
        return favoriteRepo.save(favorite);
    }

    /**
     * Remove a favorite cocktail from the user's favorites.
     */
    @Transactional
    public void removeFavorite(User user, Long cocktailId) {
        favoriteRepo.findByUser(user).stream()
                .filter(fav -> fav.getCocktail().getId().equals(cocktailId))
                .findFirst()
                .ifPresent(favoriteRepo::delete);
    }

    /**
     * Add or update a note linked to a user's favorite cocktail.
     */
    @Transactional
    public Favorite addOrUpdateNote(User user, Long cocktailId, NoteRequest request) {
        Favorite favorite = favoriteRepo.findByUser(user).stream()
                .filter(fav -> fav.getCocktail().getId().equals(cocktailId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Favorite not found for cocktailId: " + cocktailId));

        Note note = favorite.getNote();
        if (note == null) {
            note = new Note();
        }
        note.setContent(request.getContent());
        note = noteRepo.save(note);

        favorite.setNote(note);
        return favoriteRepo.save(favorite);
    }

    /**
     * Add or update a rating linked to a user's favorite cocktail.
     */
    @Transactional
    public Favorite addOrUpdateRating(User user, Long cocktailId, RatingRequest request) {
        Cocktail cocktail = cocktailRepo.findById(cocktailId)
                .orElseThrow(() -> new RuntimeException("Cocktail not found with id: " + cocktailId));

        Favorite favorite = favoriteRepo.findByUser(user).stream()
                .filter(fav -> fav.getCocktail().getId().equals(cocktailId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Favorite not found for cocktailId: " + cocktailId));

        Rating rating = favorite.getRating();
        if (rating == null) {
            rating = new Rating();
        }
        rating.setScore(request.getScore());
        rating.setCocktail(cocktail);
        rating = ratingRepo.save(rating);

        favorite.setRating(rating);
        return favoriteRepo.save(favorite);
    }
}
