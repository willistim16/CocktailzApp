package com.cocktailz.cocktailzclean.service;

import com.cocktailz.cocktailzclean.dto.NoteDto;
import com.cocktailz.cocktailzclean.entity.Note;
import com.cocktailz.cocktailzclean.entity.User;

import java.util.List;
import java.util.Optional;

public interface NoteService {
    Note addOrUpdateNote(User user, NoteDto dto, Long noteId);

    void deleteNoteById(User user, Long noteId);

    List<NoteDto> getNotesForFavorite(User user, Long favoriteId);

    List<Note> getNotesForUser(User user);

    Optional<Note> getNoteById(Long noteId);
}
