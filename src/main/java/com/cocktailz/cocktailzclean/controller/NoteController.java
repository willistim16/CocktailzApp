package com.cocktailz.cocktailzclean.controller;

import com.cocktailz.cocktailzclean.dto.NoteDto;
import com.cocktailz.cocktailzclean.entity.Note;
import com.cocktailz.cocktailzclean.entity.User;
import com.cocktailz.cocktailzclean.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@PreAuthorize("isAuthenticated()")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<NoteDto> updateNote(
            @AuthenticationPrincipal User user,
            @PathVariable Long noteId,
            @RequestBody NoteDto dto
    ) {
        Note updatedNote = noteService.addOrUpdateNote(user, dto, noteId);

        NoteDto response = new NoteDto();
        response.setId(updatedNote.getId());
        response.setFavoriteId(updatedNote.getFavorite().getId());
        response.setContent(updatedNote.getContent());
        response.setUserId(updatedNote.getUser().getId());
        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<NoteDto> addOrUpdateNote(
            @AuthenticationPrincipal User user,
            @RequestBody NoteDto dto,
            @RequestParam(required = false) Long noteId
    ) {
        Note savedNote = noteService.addOrUpdateNote(user, dto, noteId);

        NoteDto response = new NoteDto();
        response.setId(savedNote.getId());
        response.setFavoriteId(savedNote.getFavorite().getId());
        response.setContent(savedNote.getContent());
        response.setUserId(savedNote.getUser().getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(@AuthenticationPrincipal User user,
                                           @PathVariable Long noteId) {
        noteService.deleteNoteById(user, noteId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/favorite/{favoriteId}")
    public ResponseEntity<List<NoteDto>> getNotesForFavorite(@AuthenticationPrincipal User user,
                                                             @PathVariable Long favoriteId) {
        return ResponseEntity.ok(noteService.getNotesForFavorite(user, favoriteId));
    }
}
