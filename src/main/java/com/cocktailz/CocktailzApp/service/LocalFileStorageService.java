package com.cocktailz.CocktailzApp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class LocalFileStorageService implements com.cocktailz.CocktailzApp.service.FileStorageService {

    private final Path fileStorageLocation;

    public LocalFileStorageService(@Value("${file.upload-dir:uploads}") String uploadDir) throws IOException {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.fileStorageLocation);
    }

    @Override
    public String storeFile(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID() + "-" + StringUtils.cleanPath(file.getOriginalFilename());
        Path targetLocation = this.fileStorageLocation.resolve(filename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return filename;
    }

    @Override
    public Path getFilePath(String filename) {
        return fileStorageLocation.resolve(filename).normalize();
    }
}
