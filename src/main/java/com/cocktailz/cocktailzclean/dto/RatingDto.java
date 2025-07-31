package com.cocktailz.cocktailzclean.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class RatingDto {
    private Long favoriteId;

    @Min(1)
    @Max(10)
    private int score;
}
