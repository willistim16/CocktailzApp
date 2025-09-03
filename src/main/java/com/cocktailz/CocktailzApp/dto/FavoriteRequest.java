package com.cocktailz.CocktailzApp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FavoriteRequest {
    @NotNull
    private Long cocktailId;
}