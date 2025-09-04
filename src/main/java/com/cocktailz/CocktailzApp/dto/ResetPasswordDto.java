package com.cocktailz.CocktailzApp.dto;

import lombok.Data;

@Data
public class ResetPasswordDto {
    private String token;
    private String newPassword;
}