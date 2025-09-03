package com.cocktailz.CocktailzApp.mapper;

import com.cocktailz.CocktailzApp.dto.FavoriteResponseDto;
import com.cocktailz.CocktailzApp.dto.UserDTO;
import com.cocktailz.CocktailzApp.entity.Favorite;
import com.cocktailz.CocktailzApp.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static UserDTO mapToDTO(User user) {
        List<FavoriteResponseDto> favoriteDTOs = user.getFavorites() != null ? user.getFavorites().stream()
                .map(UserMapper::mapFavoriteToDto)
                .toList() : new ArrayList<>();

        return new UserDTO(user.getUsername(), user.getEmail(), favoriteDTOs);
    }

    private static FavoriteResponseDto mapFavoriteToDto(Favorite favorite) {
        FavoriteResponseDto dto = new FavoriteResponseDto();
        dto.setId(favorite.getId());
        dto.setCocktailName(favorite.getCocktail().getName());

        if (favorite.getNotes() != null && !favorite.getNotes().isEmpty()) {
            List<String> noteContents = favorite.getNotes().stream()
                    .map(n -> n.getContent())
                    .toList();
            dto.setNotes(noteContents);
        } else {
            dto.setNotes(new ArrayList<>());
        }

        dto.setRating(favorite.getRating() != null ? favorite.getRating().getScore() : null);
        return dto;
    }
}
