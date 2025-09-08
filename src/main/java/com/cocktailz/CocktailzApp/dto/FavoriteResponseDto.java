package com.cocktailz.CocktailzApp.dto;

import lombok.Data;

import java.util.List;

@Data
public class FavoriteResponseDto {
    private Long id;
    private Long cocktailId;
    private String cocktailName;
    private Integer rating;
    private String thumbnail;
    private List<String> notes;
    private String username;
}
