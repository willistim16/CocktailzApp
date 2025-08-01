package com.cocktailz.cocktailzclean.Test;

import com.cocktailz.cocktailzclean.entity.Note;
import com.cocktailz.cocktailzclean.repository.NoteRepository;
import com.cocktailz.cocktailzclean.service.NoteService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    public NoteServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetNoteById() {
        Note note = new Note();
        note.setId(1L);
        note.setContent("Lekker fris!");

        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));

        Optional<Note> result = noteService.getNoteById(1L);

        assertTrue(result.isPresent());
        assertEquals("Lekker fris!", result.get().getContent());
    }
}
