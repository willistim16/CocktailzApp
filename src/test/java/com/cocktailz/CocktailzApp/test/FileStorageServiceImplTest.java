package com.cocktailz.CocktailzApp.test;

import com.cocktailz.CocktailzApp.service.impl.FileStorageServiceImpl;
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
        fileStorageService = new FileStorageServiceImpl(uploadDir);

        Files.createDirectories(Path.of(uploadDir));
    }

    @Test
    void storeFile_shouldSaveFileAndReturnFilename() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test-image.png", "image/png", "fake-image-content".getBytes());

        String filename = fileStorageService.storeFile(file);
        Path filePath = Path.of(uploadDir).resolve(filename);

        assertNotNull(filename);
        assertTrue(Files.exists(filePath));
        assertTrue(Files.size(filePath) > 0);
    }

    @Test
    void getFilePath_shouldReturnCorrectPath() throws IOException {
        String filename = "example.png";

        Path path = fileStorageService.getFilePath(filename);

        assertEquals(Path.of(uploadDir).toAbsolutePath().resolve(filename), path);
    }
    @AfterEach
    void tearDown() throws IOException {
        Files.walk(Path.of(uploadDir))
                .sorted((a, b) -> b.compareTo(a))
                .forEach(p -> {
                    try { Files.deleteIfExists(p); } catch (IOException ignored) {}
                });
    }
}
