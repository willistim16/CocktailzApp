package com.cocktailz.cocktailzclean.service.impl;

import com.cocktailz.cocktailzclean.dto.NoteDto;
import com.cocktailz.cocktailzclean.entity.Favorite;
import com.cocktailz.cocktailzclean.entity.Note;
import com.cocktailz.cocktailzclean.entity.User;
import com.cocktailz.cocktailzclean.repository.FavoriteRepository;
import com.cocktailz.cocktailzclean.repository.NoteRepository;
import com.cocktailz.cocktailzclean.service.NoteService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NoteServiceImpl implements NoteService {

    private final FavoriteRepository favoriteRepository;
    private final NoteRepository noteRepository;

    public NoteServiceImpl(FavoriteRepository favoriteRepository, NoteRepository noteRepository) {
        this.favoriteRepository = favoriteRepository;
        this.noteRepository = noteRepository;
    }

    @Override
    @Transactional
    public Note addOrUpdateNote(User user, NoteDto dto, Long noteId) {
        Favorite favorite = favoriteRepository.findById(dto.getFavoriteId())
                .filter(f -> f.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Favorite not found"));

        Note note;
        if (noteId != null) {
            note = noteRepository.findById(noteId)
                    .filter(n -> n.getUser().getId().equals(user.getId()))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));
        } else {
            note = new Note();
            note.setFavorite(favorite);
            note.setUser(user);
        }

        note.setContent(dto.getContent());
        return noteRepository.save(note);
    }

    @Override
    @Transactional
    public void deleteNoteById(User user, Long noteId) {
        Note note = noteRepository.findById(noteId)
                .filter(n -> n.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));

        noteRepository.delete(note);
    }

    @Override
    @Transactional
    public List<NoteDto> getNotesForFavorite(User user, Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .filter(f -> f.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Favorite not found"));

        List<NoteDto> dtoList = new ArrayList<>();
        for (Note note : favorite.getNotes()) {
            NoteDto dto = new NoteDto();
            dto.setFavoriteId(favorite.getId());
            if (note.getUser() != null) {
                dto.setUserId(note.getUser().getId());
            } else {
                dto.setUserId(null);
            }
            dto.setContent(note.getContent());
            dto.setId(note.getId());
            dtoList.add(dto);
        }
        return dtoList;
    }


    @Override
    @Transactional
    public List<Note> getNotesForUser(User user) {
        return noteRepository.findByUser(user);
    }

    @Override
    public Optional<Note> getNoteById(Long noteId) {
        return noteRepository.findById(noteId);
    }
}
