package com.cocktailz.cocktailzclean.dto;

import lombok.Data;

@Data
public class FavoriteResponseDto {
    private Long id;
    private String cocktailName;
    private String note;
    private Integer rating;
}
