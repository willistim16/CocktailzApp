package com.cocktailz.cocktailzclean.service;

import com.cocktailz.cocktailzclean.entity.User;
import java.util.List;

public interface UserService {

    User registerUser(String username, String email, String encodedPassword);

    User findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> getAllUsers();
}
