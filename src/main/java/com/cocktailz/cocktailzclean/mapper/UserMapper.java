package com.cocktailz.cocktailzclean.mapper;

import com.cocktailz.cocktailzclean.dto.FavoriteResponseDto;
import com.cocktailz.cocktailzclean.dto.UserDTO;
import com.cocktailz.cocktailzclean.entity.Favorite;
import com.cocktailz.cocktailzclean.entity.User;

import java.util.List;

public class UserMapper {

    public static UserDTO mapToDTO(User user) {
        List<FavoriteResponseDto> favoriteDTOs = user.getFavorites().stream()
                .map(UserMapper::mapFavoriteToDto)
                .toList();

        return new UserDTO(user.getUsername(), user.getEmail(), favoriteDTOs);
    }

    private static FavoriteResponseDto mapFavoriteToDto(Favorite favorite) {
        FavoriteResponseDto dto = new FavoriteResponseDto();
        dto.setId(favorite.getId());
        dto.setCocktailName(favorite.getCocktail().getName());
        dto.setNote(favorite.getNote() != null ? favorite.getNote().getContent() : null);
        dto.setRating(favorite.getRating() != null ? favorite.getRating().getScore() : null);
        return dto;
    }
}
