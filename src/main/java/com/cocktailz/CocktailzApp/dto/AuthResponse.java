package com.cocktailz.CocktailzApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class AuthResponse {
    @JsonProperty("jwt")
    private String jwt;
    private Long id;
    private String username;
    private String email;
    private String profileImagePath;

    public AuthResponse(String jwt, Long id, String username, String email, String profileImagePath) {
        this.jwt = jwt;
        this.id = id;
        this.username = username;
        this.email = email;
        this.profileImagePath = profileImagePath;
    }
}
