package com.cocktailz.cocktailzclean.controller;

import com.cocktailz.cocktailzclean.entity.Cocktail;
import com.cocktailz.cocktailzclean.service.CocktailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cocktails")
public class CocktailController {

    private final CocktailService cocktailService;

    public CocktailController(CocktailService cocktailService) {
        this.cocktailService = cocktailService;
    }

    // 1. Get all cocktails
    @GetMapping
    public List<Cocktail> getAllCocktails() {
        return cocktailService.getAllCocktails();
    }

    // 2. Search by name (case-insensitive)
    @GetMapping("/search")
    public List<Cocktail> searchByName(@RequestParam String name) {
        return cocktailService.searchByName(name);
    }

    // 3. Filter by alcoholic (Boolean)
    @GetMapping("/filter")
    public List<Cocktail> filterByAlcoholic(@RequestParam Boolean alcoholic) {
        return cocktailService.filterByAlcoholic(alcoholic);
    }

    // 4. Get random cocktails
    @GetMapping("/random")
    public ResponseEntity<List<Cocktail>> getRandomCocktails(@RequestParam(defaultValue = "3") int count) {
        List<Cocktail> randomCocktails = cocktailService.getRandomCocktails(count);
        return ResponseEntity.ok(randomCocktails);
    }
    @GetMapping("/test-random")
    public ResponseEntity<List<Cocktail>> testRandomCocktails() {
        List<Cocktail> randomCocktails = cocktailService.getRandomCocktails(3);
        System.out.println("Random cocktails fetched: " + randomCocktails.size());
        randomCocktails.forEach(c -> System.out.println(c.getName()));
        return ResponseEntity.ok(randomCocktails);
    }
}

