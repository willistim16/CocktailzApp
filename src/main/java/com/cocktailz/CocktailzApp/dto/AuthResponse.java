package com.cocktailz.CocktailzApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class AuthResponse {
    @JsonProperty("jwt")
    private String token;
    private Long id;
    private String username;
    private String email;
    private String profileImagePath;

    public AuthResponse(String token, Long id, String username, String email, String profileImagePath) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.profileImagePath = profileImagePath;
    }
}
