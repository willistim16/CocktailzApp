package com.cocktailz.CocktailzApp.service;

import com.cocktailz.CocktailzApp.dto.CocktailDto;
import com.cocktailz.CocktailzApp.entity.Cocktail;
import com.cocktailz.CocktailzApp.repository.CocktailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CocktailService {

    private final CocktailRepository cocktailRepository;

    private CocktailDto mapToDto(Cocktail cocktail) {

        Double averageRating = (cocktail.getRatings() == null || cocktail.getRatings().isEmpty())
                ? null
                : cocktail.getRatings().stream().mapToInt(r -> r.getScore()).average().orElse(0);

        return new CocktailDto(
                cocktail.getId(),
                cocktail.getExternalId(),
                cocktail.getIdDrink(),
                cocktail.getName(),
                cocktail.getCategory(),
                cocktail.getGlass(),
                cocktail.getAlcoholic(),
                cocktail.getInstructions(),
                cocktail.getIngredient(),
                cocktail.getImageUrl(),
                averageRating
        );
    }

    public List<String> getAllCategories() {
        return cocktailRepository.findDistinctCategories();
    }

    public List<String> getAllGlasses() {
        return cocktailRepository.findDistinctGlasses();
    }


    public List<CocktailDto> getAllCocktailsDto() {
        return cocktailRepository.findAll().stream().map(this::mapToDto).toList();
    }

    public List<CocktailDto> searchByNameDto(String name) {
        return cocktailRepository.findByNameContainingIgnoreCase(name).stream().map(this::mapToDto).toList();
    }

    public List<CocktailDto> findByIngredientDto(String ingredient) {
        return cocktailRepository.findByIngredientContainingIgnoreCase(ingredient).stream().map(this::mapToDto).toList();
    }

    public CocktailDto getCocktailByIdDto(Long id) {
        return cocktailRepository.findById(id).map(this::mapToDto).orElse(null);
    }

    public List<CocktailDto> getRandomCocktailsDto(int count) {
        List<Cocktail> randoms = cocktailRepository.findRandomCocktails(count);
        if (randoms.isEmpty()) throw new RuntimeException("No cocktails available in the database.");
        return randoms.stream().map(this::mapToDto).toList();
    }

    public List<CocktailDto> filterCocktailsDto(Boolean alcoholic, String category, String glass) {
        List<Cocktail> filtered;
        if (alcoholic != null && category != null && glass != null) {
            filtered = cocktailRepository.findByAlcoholicAndCategoryAndGlass(alcoholic, category, glass);
        } else if (alcoholic != null && category != null) {
            filtered = cocktailRepository.findByAlcoholicAndCategory(alcoholic, category);
        } else if (alcoholic != null && glass != null) {
            filtered = cocktailRepository.findByAlcoholicAndGlass(alcoholic, glass);
        } else if (category != null && glass != null) {
            filtered = cocktailRepository.findByCategoryAndGlass(category, glass);
        } else if (alcoholic != null) {
            filtered = cocktailRepository.findByAlcoholic(alcoholic);
        } else if (category != null) {
            filtered = cocktailRepository.findByCategory(category);
        } else if (glass != null) {
            filtered = cocktailRepository.findByGlass(glass);
        } else {
            filtered = cocktailRepository.findAll();
        }
        return filtered.stream().map(this::mapToDto).toList();
    }
}
