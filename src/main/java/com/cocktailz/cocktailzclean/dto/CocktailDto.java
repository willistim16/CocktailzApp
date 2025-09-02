package com.cocktailz.cocktailzclean.dto;

import lombok.Data;

@Data
public class CocktailDto {
    private Long id;
    private String externalId;
    private String idDrink;
    private String name;
    private String category;
    private String glass;
    private Boolean alcoholic;
    private String instructions;
    private String ingredient;
    private String imageUrl;
    private Double averageRating;

    public CocktailDto(Long id, String externalId, String idDrink, String name, String category,
                       String glass, Boolean alcoholic, String instructions, String ingredient, String imageUrl,  Double averageRating) {
        this.id = id;
        this.externalId = externalId;
        this.idDrink = idDrink;
        this.name = name;
        this.category = category;
        this.glass = glass;
        this.alcoholic = alcoholic;
        this.instructions = instructions;
        this.ingredient = ingredient;
        this.imageUrl = imageUrl;
        this.averageRating = averageRating;
    }
}
