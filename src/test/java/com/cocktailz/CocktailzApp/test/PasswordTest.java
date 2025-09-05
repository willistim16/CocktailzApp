package com.cocktailz.CocktailzApp.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordTest {
    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matches = encoder.matches(
                "Admin123!",
                "$2a$10$XehJshAEP1qZyvh5nHiYlu8q0FrzKMS1JlGgD2ebM9ro.EAZscSuq"
        );
        System.out.println("Matches? " + matches);
    }
}
