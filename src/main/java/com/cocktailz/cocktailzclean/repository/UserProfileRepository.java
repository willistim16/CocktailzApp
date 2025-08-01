package com.cocktailz.cocktailzclean.repository;

import com.cocktailz.cocktailzclean.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}
