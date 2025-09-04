package com.cocktailz.CocktailzApp.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDto {
    private Long cocktailId;

    @Min(1)
    @Max(5)
    private int score;
}
