package com.cocktailz.cocktailzclean.service;

import com.cocktailz.cocktailzclean.entity.Favorite;
import com.cocktailz.cocktailzclean.entity.Note;
import com.cocktailz.cocktailzclean.entity.User;
import com.cocktailz.cocktailzclean.dto.NoteDto;
import com.cocktailz.cocktailzclean.repository.FavoriteRepository;
import com.cocktailz.cocktailzclean.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NoteService {

    private final FavoriteRepository favoriteRepository;
    private final NoteRepository noteRepository;

    public NoteService(FavoriteRepository favoriteRepository, NoteRepository noteRepository) {
        this.favoriteRepository = favoriteRepository;
        this.noteRepository = noteRepository;
    }

    /**
     * Add or update a note linked to a user's favorite cocktail.
     */
    public Note addOrUpdateNote(User user, NoteDto dto) {
        Favorite favorite = favoriteRepository.findById(dto.getFavoriteId())
                .filter(f -> f.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Favorite not found or not authorized"));

        Note note = favorite.getNote();
        if (note == null) {
            note = new Note();
        }
        note.setContent(dto.getContent());
        Note saved = noteRepository.save(note);

        favorite.setNote(saved);
        favoriteRepository.save(favorite);

        return saved;
    }

    /**
     * Delete the note linked to a user's favorite cocktail.
     */
    public void deleteNote(User user, Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .filter(f -> f.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Favorite not found or not authorized"));

        Note note = favorite.getNote();
        if (note != null) {
            favorite.setNote(null);
            favoriteRepository.save(favorite);
            noteRepository.delete(note);
        }
    }

    /**
     * Find a note by its ID.
     */
    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }
}
