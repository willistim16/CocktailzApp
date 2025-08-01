package com.cocktailz.cocktailzclean.dto;

import lombok.Data;

import java.util.List;

@Data
public class CocktailDbResponse {
    private List<DrinkDto> drinks;

    @Data
    public static class Drink {
        private String strDrink;
        private String strInstructions;
        private String strDrinkThumb;
        private String strAlcoholic;
    }
}
