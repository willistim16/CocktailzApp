package com.cocktailz.cocktailzclean.service;

import com.cocktailz.cocktailzclean.entity.Cocktail;
import com.cocktailz.cocktailzclean.repository.CocktailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CocktailService {

    private final CocktailRepository cocktailRepository;

    public List<Cocktail> getAllCocktails() {
        return cocktailRepository.findAll();
    }

    public List<Cocktail> searchByName(String name) {
        return cocktailRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Cocktail> filterByAlcoholic(Boolean alcoholic) {
        return cocktailRepository.findByAlcoholic(alcoholic);
    }

    public List<Cocktail> getRandomCocktails(int count) {
        List<Cocktail> randoms = cocktailRepository.findRandomCocktails(count);
        if (randoms.isEmpty()) {
            throw new RuntimeException("No cocktails available in the database.");
        }
        return randoms;
    }

}
