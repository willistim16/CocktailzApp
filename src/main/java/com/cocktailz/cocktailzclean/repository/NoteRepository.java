package com.cocktailz.cocktailzclean.repository;

import com.cocktailz.cocktailzclean.Entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
    // JpaRepository already provides:
    // - findById
    // - save
    // - deleteById
    // - findAll
}
