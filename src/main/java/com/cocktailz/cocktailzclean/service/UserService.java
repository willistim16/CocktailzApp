package com.cocktailz.cocktailzclean.service;

import com.cocktailz.cocktailzclean.entity.User;

public interface UserService {
    User registerUser(String username, String email, String password);

    // eventueel extra methodes
    User findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
