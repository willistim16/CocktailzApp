package com.cocktailz.cocktailzclean.controller;

import com.cocktailz.cocktailzclean.Entity.User;
import com.cocktailz.cocktailzclean.dto.NoteDto;
import com.cocktailz.cocktailzclean.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addOrUpdateNote(@RequestBody NoteDto noteDto,
                                             @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(noteService.addOrUpdateNote(user, noteDto));
    }

    @DeleteMapping("/{favoriteId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteNote(@PathVariable Long favoriteId,
                                        @AuthenticationPrincipal User user) {
        noteService.deleteNote(user, favoriteId);
        return ResponseEntity.noContent().build();
    }
}
