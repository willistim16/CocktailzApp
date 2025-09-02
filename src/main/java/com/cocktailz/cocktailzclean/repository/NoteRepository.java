package com.cocktailz.cocktailzclean.repository;

import com.cocktailz.cocktailzclean.entity.Note;
import com.cocktailz.cocktailzclean.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    Optional<Note> findByUserAndFavoriteId(User user, Long favoriteId);

    Optional<Note> findByFavoriteId(Long favoriteId);

    List<Note> findByUser(User user);
}
