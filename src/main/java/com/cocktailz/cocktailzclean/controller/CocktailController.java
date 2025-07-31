package com.cocktailz.cocktailzclean.controller;

import com.cocktailz.cocktailzclean.Entity.Cocktail;
import com.cocktailz.cocktailzclean.repository.CocktailRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cocktails")
@CrossOrigin(origins = "*") // Pas dit aan naar je frontend URL in productie
public class CocktailController {

    private final CocktailRepository cocktailRepository;

    public CocktailController(CocktailRepository cocktailRepository) {
        this.cocktailRepository = cocktailRepository;
    }

    // 1. Alle cocktails ophalen
    @GetMapping
    public List<Cocktail> getAllCocktails() {
        return cocktailRepository.findAll();
    }

    // 2. Zoeken op naam (case-insensitive)
    @GetMapping("/search")
    public List<Cocktail> searchByName(@RequestParam String name) {
        return cocktailRepository.findByNameContainingIgnoreCase(name);
    }

    // 3. Filteren op alcoholic (true of false)
    @GetMapping("/filter")
    public List<Cocktail> filterByAlcoholic(@RequestParam boolean alcoholic) {
        return cocktailRepository.findByAlcoholic(alcoholic);
    }
}
