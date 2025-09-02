package com.cocktailz.cocktailzclean.test;

import com.cocktailz.cocktailzclean.dto.NoteDto;
import com.cocktailz.cocktailzclean.entity.Favorite;
import com.cocktailz.cocktailzclean.entity.Note;
import com.cocktailz.cocktailzclean.entity.User;
import com.cocktailz.cocktailzclean.repository.FavoriteRepository;
import com.cocktailz.cocktailzclean.repository.NoteRepository;
import com.cocktailz.cocktailzclean.service.impl.NoteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NoteServiceImplTest {

    private FavoriteRepository favoriteRepository;
    private NoteRepository noteRepository;
    private NoteServiceImpl noteService;

    private User user;
    private Favorite favorite;
    private Note note;
    private NoteDto noteDto;

    @BeforeEach
    void setUp() {
        favoriteRepository = mock(FavoriteRepository.class);
        noteRepository = mock(NoteRepository.class);
        noteService = new NoteServiceImpl(favoriteRepository, noteRepository);

        user = new User();
        user.setId(1L);

        favorite = new Favorite();
        favorite.setId(101L);
        favorite.setUser(user);

        note = new Note();
        note.setId(201L);
        note.setUser(user);
        note.setFavorite(favorite);
        note.setContent("Test note");

        noteDto = new NoteDto();
        noteDto.setFavoriteId(favorite.getId());
        noteDto.setContent("Test note");
    }

    // ---------------------- addOrUpdateNote ----------------------
    @Test
    void testAddOrUpdateNote_NewNote() {
        when(favoriteRepository.findById(favorite.getId())).thenReturn(Optional.of(favorite));
        when(noteRepository.save(any(Note.class))).thenReturn(note);

        Note result = noteService.addOrUpdateNote(user, noteDto, null);
        assertNotNull(result);
        assertEquals("Test note", result.getContent());
        verify(noteRepository).save(any(Note.class));
    }

    @Test
    void testAddOrUpdateNote_UpdateExisting() {
        when(favoriteRepository.findById(favorite.getId())).thenReturn(Optional.of(favorite));
        when(noteRepository.findById(note.getId())).thenReturn(Optional.of(note));
        when(noteRepository.save(any(Note.class))).thenReturn(note);

        Note result = noteService.addOrUpdateNote(user, noteDto, note.getId());
        assertNotNull(result);
        assertEquals("Test note", result.getContent());
        verify(noteRepository).save(any(Note.class));
    }

    @Test
    void testAddOrUpdateNote_FavoriteNotFound() {
        when(favoriteRepository.findById(favorite.getId())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> noteService.addOrUpdateNote(user, noteDto, null));
    }

    @Test
    void testAddOrUpdateNote_NoteNotFound() {
        when(favoriteRepository.findById(favorite.getId())).thenReturn(Optional.of(favorite));
        when(noteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> noteService.addOrUpdateNote(user, noteDto, 999L));
    }

    // ---------------------- deleteNoteById ----------------------
    @Test
    void testDeleteNoteById() {
        when(noteRepository.findById(note.getId())).thenReturn(Optional.of(note));
        noteService.deleteNoteById(user, note.getId());
        verify(noteRepository).delete(note);
    }

    @Test
    void testDeleteNoteById_NotFound() {
        when(noteRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> noteService.deleteNoteById(user, 999L));
    }

    @Test
    void deleteNoteById_ShouldThrow_WhenNoteBelongsToAnotherUser() {
        // Arrange
        User loggedInUser = new User();
        loggedInUser.setId(1L);

        User otherUser = new User();
        otherUser.setId(2L);

        Note note = new Note();
        note.setId(99L);
        note.setUser(otherUser); // note owned by someone else

        when(noteRepository.findById(99L)).thenReturn(Optional.of(note));

        // Act + Assert
        assertThrows(ResponseStatusException.class,
                () -> noteService.deleteNoteById(loggedInUser, 99L));
    }


    // ---------------------- getNotesForFavorite ----------------------
    @Test
    void testGetNotesForFavorite() {
        favorite.setNotes(List.of(note));
        when(favoriteRepository.findById(favorite.getId())).thenReturn(Optional.of(favorite));

        List<NoteDto> result = noteService.getNotesForFavorite(user, favorite.getId());
        assertEquals(1, result.size());
        assertEquals("Test note", result.get(0).getContent());
    }

    @Test
    void getNotesForFavorite_NoteWithUser() {
        favorite.setNotes(List.of(note)); // note.user != null
        when(favoriteRepository.findById(favorite.getId())).thenReturn(Optional.of(favorite));

        List<NoteDto> result = noteService.getNotesForFavorite(user, favorite.getId());
        assertEquals(1, result.size());
        assertEquals(user.getId(), result.get(0).getUserId());
    }

    @Test
    void getNotesForFavorite_EmptyNotes() {
        favorite.setNotes(List.of()); // empty list
        when(favoriteRepository.findById(favorite.getId())).thenReturn(Optional.of(favorite));

        List<NoteDto> result = noteService.getNotesForFavorite(user, favorite.getId());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getNotesForFavorite_NoteWithNullUser() {
        Note noteWithNullUser = new Note();
        noteWithNullUser.setId(202L);
        noteWithNullUser.setUser(null);
        noteWithNullUser.setContent("No owner");

        favorite.setNotes(List.of(noteWithNullUser));
        when(favoriteRepository.findById(favorite.getId())).thenReturn(Optional.of(favorite));

        List<NoteDto> result = noteService.getNotesForFavorite(user, favorite.getId());
        assertEquals(1, result.size());
        assertNull(result.get(0).getUserId());
        assertEquals("No owner", result.get(0).getContent());
    }

    @Test
    void testGetNotesForFavorite_ShouldThrow_WhenFavoriteBelongsToAnotherUser() {
        // Arrange
        User loggedInUser = new User();
        loggedInUser.setId(1L);

        User otherUser = new User();
        otherUser.setId(2L);

        Favorite favorite = new Favorite();
        favorite.setId(10L);
        favorite.setUser(otherUser); // belongs to someone else
        favorite.setNotes(List.of(new Note()));

        when(favoriteRepository.findById(10L)).thenReturn(Optional.of(favorite));

        // Act + Assert
        assertThrows(ResponseStatusException.class,
                () -> noteService.getNotesForFavorite(loggedInUser, 10L));
    }


    @Test
    void testGetNotesForFavorite_NotFound() {
        when(favoriteRepository.findById(favorite.getId())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> noteService.getNotesForFavorite(user, favorite.getId()));
    }

    // ---------------------- getNotesForUser ----------------------
    @Test
    void testGetNotesForUser() {
        when(noteRepository.findByUser(user)).thenReturn(List.of(note));
        List<Note> result = noteService.getNotesForUser(user);
        assertEquals(1, result.size());
    }

    // ---------------------- getNoteById ----------------------
    @Test
    void testGetNoteById() {
        when(noteRepository.findById(note.getId())).thenReturn(Optional.of(note));
        Optional<Note> result = noteService.getNoteById(note.getId());
        assertTrue(result.isPresent());
    }
}
