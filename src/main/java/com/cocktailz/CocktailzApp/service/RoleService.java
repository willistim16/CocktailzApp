package com.cocktailz.CocktailzApp.service;

import com.cocktailz.CocktailzApp.entity.Role;
import com.cocktailz.CocktailzApp.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    List<Role> findAll();

    Optional<Role> findById(Long id);

    Optional<Role> findByName(String name);
    
    Role save(Role role);

    void deleteById(Long id);
}
