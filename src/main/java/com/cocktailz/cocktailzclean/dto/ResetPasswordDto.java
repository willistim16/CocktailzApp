package com.cocktailz.cocktailzclean.dto;

import lombok.Data;

@Data
public class ResetPasswordDto {
    private String token;
    private String newPassword;
}