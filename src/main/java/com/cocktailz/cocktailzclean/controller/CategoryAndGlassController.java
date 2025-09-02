package com.cocktailz.cocktailzclean.controller;

import com.cocktailz.cocktailzclean.service.CocktailService;
import jakarta.persistence.Table;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryAndGlassController {

    private final CocktailService cocktailService;

    public CategoryAndGlassController(CocktailService cocktailService) {
        this.cocktailService = cocktailService;
    }

    @GetMapping("/categories")
    @PreAuthorize("isAuthenticated()")
    public List<String> getAllCategories() {
        return cocktailService.getAllCategories();
    }
    @GetMapping("/glasses")
    @PreAuthorize("isAuthenticated()")
    public List<String> getAllGlasses() {
        return cocktailService.getAllGlasses();
    }
}
