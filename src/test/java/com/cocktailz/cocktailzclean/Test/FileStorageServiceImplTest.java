package com.cocktailz.cocktailzclean.Test;

import com.cocktailz.cocktailzclean.service.FileStorageServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileStorageServiceImplTest {

    private FileStorageServiceImpl fileStorageService;
    private final String uploadDir = "test-uploads";

    @BeforeEach
    void setUp() throws IOException {
        // Maak een nieuwe service aan met testmap
        fileStorageService = new FileStorageServiceImpl(uploadDir);

        // Zorg dat testmap leeg is
        Files.createDirectories(Path.of(uploadDir));
    }

    @Test
    void storeFile_shouldSaveFileAndReturnFilename() throws IOException {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file", "test-image.png", "image/png", "fake-image-content".getBytes());

        // Act
        String filename = fileStorageService.storeFile(file);
        Path filePath = Path.of(uploadDir).resolve(filename);

        // Assert
        assertNotNull(filename);
        assertTrue(Files.exists(filePath));
        assertTrue(Files.size(filePath) > 0);
    }

    @Test
    void getFilePath_shouldReturnCorrectPath() throws IOException {
        // Arrange
        String filename = "example.png";

        // Act
        Path path = fileStorageService.getFilePath(filename);

        // Assert
        assertEquals(Path.of(uploadDir).toAbsolutePath().resolve(filename), path);
    }
    @AfterEach
    void tearDown() throws IOException {
        Files.walk(Path.of(uploadDir))
                .sorted((a, b) -> b.compareTo(a)) // eerst bestanden, dan map
                .forEach(p -> {
                    try { Files.deleteIfExists(p); } catch (IOException ignored) {}
                });
    }
}
