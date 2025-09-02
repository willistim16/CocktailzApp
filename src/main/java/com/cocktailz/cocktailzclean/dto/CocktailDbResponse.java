package com.cocktailz.cocktailzclean.dto;

import lombok.Data;

import java.util.List;

@Data
public class CocktailDbResponse {
    private List<Drink> drinks;

    @Data
    public static class Drink {
        private String idDrink;
        private String strDrink;
        private String strCategory;
        private String strAlcoholic;
        private String strGlass;
        private String strInstructions;
        private String strDrinkThumb;

        // ✅ All 15 ingredients
        private String strIngredient1;
        private String strIngredient2;
        private String strIngredient3;
        private String strIngredient4;
        private String strIngredient5;
        private String strIngredient6;
        private String strIngredient7;
        private String strIngredient8;
        private String strIngredient9;
        private String strIngredient10;
        private String strIngredient11;
        private String strIngredient12;
        private String strIngredient13;
        private String strIngredient14;
        private String strIngredient15;
    }
}
