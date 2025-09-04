package com.cocktailz.CocktailzApp.test;

import com.cocktailz.CocktailzApp.dto.AuthResponse;
import com.cocktailz.CocktailzApp.entity.User;
import com.cocktailz.CocktailzApp.entity.PasswordResetToken;
import com.cocktailz.CocktailzApp.service.AuthService;
import com.cocktailz.CocktailzApp.service.EmailService;
import com.cocktailz.CocktailzApp.service.UserService;
import com.cocktailz.CocktailzApp.security.jwt.JwtUtil;
import com.cocktailz.CocktailzApp.repository.PasswordResetTokenRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock private JwtUtil jwtUtil;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private UserService userService;
    @Mock private PasswordResetTokenRepository tokenRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private EmailService emailService;

    @InjectMocks private AuthService authService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testRegisterSuccess() {
        String username = "user1";
        String email = "user1@test.com";
        String password = "password";

        User user = new User();
        user.setId(1L);
        user.setUsername(username);
        user.setEmail(email);

        when(userService.existsByUsername(username)).thenReturn(false);
        when(userService.existsByEmail(email)).thenReturn(false);
        when(userService.registerUser(username, email, password)).thenReturn(user);
        when(jwtUtil.generateToken(user)).thenReturn("jwt-token");

        AuthResponse response = authService.register(username, email, password);

        assertNotNull(response);
        assertEquals(username, response.getUsername());
        assertEquals(email, response.getEmail());
        assertEquals("jwt-token", response.getJwt());
        verify(userService, times(1)).registerUser(username, email, password);
    }

    @Test
    void testRegisterFailsIfUsernameExists() {
        when(userService.existsByUsername("user1")).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                authService.register("user1", "email@test.com", "pass"));

        assertEquals("Gebruikersnaam is al in gebruik.", exception.getMessage());
    }

    @Test
    void testLoginSuccess() {
        String username = "user1";
        String password = "password";

        User user = new User();
        user.setUsername(username);
        user.setId(1L);

        when(userService.findByUsername(username)).thenReturn(user);
        when(jwtUtil.generateToken(user)).thenReturn("jwt-token");

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);

        AuthResponse response = authService.login(username, password);

        assertNotNull(response);
        assertEquals("jwt-token", response.getJwt());
        assertEquals(username, response.getUsername());
    }

    @Test
    void testSendResetEmail() {
        String email = "user@test.com";

        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        when(userService.findByEmail(email)).thenReturn(user);

        authService.sendResetEmail(email);

        verify(tokenRepository, times(1)).save(any(PasswordResetToken.class));
        verify(emailService, times(1)).sendEmail(eq(email), anyString(), contains("reset"));
    }

    @Test
    void testResetPasswordSuccess() {
        String token = UUID.randomUUID().toString();
        String newPassword = "newPass";

        User user = new User();
        user.setId(1L);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(java.time.LocalDateTime.now().plusMinutes(10));

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(resetToken));

        authService.resetPassword(token, newPassword);

        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(userService, times(1)).updateUser(user);
        verify(tokenRepository, times(1)).delete(resetToken);
    }
}
