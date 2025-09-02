package com.cocktailz.cocktailzclean.service.impl;

import com.cocktailz.cocktailzclean.dto.FavoriteResponseDto;
import com.cocktailz.cocktailzclean.entity.Cocktail;
import com.cocktailz.cocktailzclean.entity.Favorite;
import com.cocktailz.cocktailzclean.entity.Note;
import com.cocktailz.cocktailzclean.entity.User;
import com.cocktailz.cocktailzclean.exception.DuplicateFavoriteException;
import com.cocktailz.cocktailzclean.repository.CocktailRepository;
import com.cocktailz.cocktailzclean.repository.FavoriteRepository;
import com.cocktailz.cocktailzclean.service.FavoriteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final CocktailRepository cocktailRepository;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository,
                               CocktailRepository cocktailRepository) {
        this.favoriteRepository = favoriteRepository;
        this.cocktailRepository = cocktailRepository;
    }

    @Override
    public List<FavoriteResponseDto> getFavoritesByUser(User user) {
        return favoriteRepository.findByUserWithNotes(user).stream()
                .map(this::mapToDto)
                .filter(Objects::nonNull) // skip favorites with null cocktail or user
                .toList();
    }

    @Override
    public Optional<Favorite> getFavoriteById(Long id) {
        return favoriteRepository.findById(id);
    }

    @Override
    public Optional<Favorite> getFavoriteByUserAndCocktailId(User user, Long cocktailId) {
        return favoriteRepository.findByUserAndCocktail_Id(user, cocktailId);
    }

    @Override
    public void deleteFavorite(User user, Long cocktailId) {
        favoriteRepository.findByUserAndCocktail_Id(user, cocktailId)
                .ifPresent(favoriteRepository::delete);
    }

    @Override
    public Favorite addFavoriteByCocktailId(User user, Long cocktailId) {
        Cocktail cocktail = cocktailRepository.findById(cocktailId)
                .orElseThrow(() -> new RuntimeException("Cocktail not found"));

        boolean exists = favoriteRepository.existsByUserAndCocktail_Id(user, cocktailId);
        if (exists) {
            throw new DuplicateFavoriteException("Cocktail already exists in favorites");
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setCocktail(cocktail);
        return favoriteRepository.save(favorite);
    }

    @Override
    public FavoriteResponseDto mapToDto(Favorite favorite) {
        if (favorite == null || favorite.getCocktail() == null || favorite.getUser() == null) {
            return null; // safe null-check to remove warnings
        }

        FavoriteResponseDto dto = new FavoriteResponseDto();
        dto.setId(favorite.getId());
        dto.setCocktailId(favorite.getCocktail().getId());
        dto.setCocktailName(favorite.getCocktail().getName());
        dto.setThumbnail(favorite.getCocktail().getImageUrl());
        dto.setUsername(favorite.getUser().getUsername());

        if (favorite.getRating() != null) {
            dto.setRating(favorite.getRating().getScore());
        }

        // ✅ altijd lege lijst als er geen notes zijn
        dto.setNotes(favorite.getNotes() != null
                ? favorite.getNotes().stream().map(Note::getContent).toList()
                : List.of());

        return dto;
    }
}
