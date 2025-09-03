package com.cocktailz.CocktailzApp.repository;

import com.cocktailz.CocktailzApp.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}
