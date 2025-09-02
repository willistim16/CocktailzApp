package com.cocktailz.cocktailzclean.controller;

import com.cocktailz.cocktailzclean.dto.CocktailDto;
import com.cocktailz.cocktailzclean.service.CocktailService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cocktails")
public class CocktailController {

    private final CocktailService cocktailService;

    public CocktailController(CocktailService cocktailService) {
        this.cocktailService = cocktailService;
    }

    @GetMapping("/home-random")
    public ResponseEntity<List<CocktailDto>> getHomeRandomCocktails() {
        return ResponseEntity.ok(cocktailService.getRandomCocktailsDto(3));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<CocktailDto> getAllCocktails() {
        return cocktailService.getAllCocktailsDto();
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public List<CocktailDto> searchByName(@RequestParam String name) {
        return cocktailService.searchByNameDto(name);
    }

    @GetMapping("/search/by-ingredient")
    @PreAuthorize("isAuthenticated()")
    public List<CocktailDto> searchByIngredient(@RequestParam String ingredient) {
        return cocktailService.findByIngredientDto(ingredient);
    }

    @GetMapping("/filter")
    @PreAuthorize("isAuthenticated()")
    public List<CocktailDto> filterCocktails(
            @RequestParam(required = false) Boolean alcoholic,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String glass
    ) {
        return cocktailService.filterCocktailsDto(alcoholic, category, glass);
    }

    @GetMapping("/random")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CocktailDto>> getRandomCocktails(@RequestParam(defaultValue = "3") int count) {
        return ResponseEntity.ok(cocktailService.getRandomCocktailsDto(count));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CocktailDto> getCocktailById(@PathVariable Long id) {
        CocktailDto dto = cocktailService.getCocktailByIdDto(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }
}
