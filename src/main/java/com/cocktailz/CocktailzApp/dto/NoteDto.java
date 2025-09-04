package com.cocktailz.CocktailzApp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NoteDto {

    @NotBlank
    private String content;

    private Long favoriteId;

    private Long userId;

    private Long id;
}
