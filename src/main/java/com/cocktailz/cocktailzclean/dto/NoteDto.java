package com.cocktailz.cocktailzclean.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class NoteDto {

    @NotBlank
    private String content;

    private Long favoriteId;

    private Long userId;

    private Long id;
}
