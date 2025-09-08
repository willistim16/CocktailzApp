package com.cocktailz.CocktailzApp.service.impl;

import com.cocktailz.CocktailzApp.dto.FavoriteResponseDto;
import com.cocktailz.CocktailzApp.entity.Cocktail;
import com.cocktailz.CocktailzApp.entity.Favorite;
import com.cocktailz.CocktailzApp.entity.Note;
import com.cocktailz.CocktailzApp.entity.User;
import com.cocktailz.CocktailzApp.exception.DuplicateFavoriteException;
import com.cocktailz.CocktailzApp.repository.CocktailRepository;
import com.cocktailz.CocktailzApp.repository.FavoriteRepository;
import com.cocktailz.CocktailzApp.service.FavoriteService;
import org.springframework.dao.DataIntegrityViolationException;
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
    @Transactional(readOnly = true)
    public List<FavoriteResponseDto> getFavoritesByUser(User user) {
        return favoriteRepository.findByUserWithNotes(user).stream()
                .map(this::mapToDto)
                .filter(Objects::nonNull)
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
    @Transactional
    public Favorite addFavoriteByCocktailId(User user, Long cocktailId) {
        Optional<Favorite> existing = favoriteRepository.findByUserAndCocktail_Id(user, cocktailId);
        if (existing.isPresent()) {
            return existing.get();
        }

        Cocktail cocktail = cocktailRepository.findById(cocktailId)
                .orElseThrow(() -> new RuntimeException("Cocktail not found"));

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setCocktail(cocktail);

        try {
            return favoriteRepository.saveAndFlush(favorite);
        } catch (DataIntegrityViolationException e) {
            return favoriteRepository.findByUserAndCocktail_Id(user, cocktailId)
                    .orElseThrow(() -> new RuntimeException("Favorite could not be created"));
        }
    }



    @Override
    public FavoriteResponseDto mapToDto(Favorite favorite) {
        if (favorite == null || favorite.getCocktail() == null || favorite.getUser() == null) {
            return null;
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

        dto.setNotes(favorite.getNotes() != null
                ? favorite.getNotes().stream().map(Note::getContent).toList()
                : List.of());

        return dto;
    }
}
