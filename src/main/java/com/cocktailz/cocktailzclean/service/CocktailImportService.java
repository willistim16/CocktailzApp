package com.cocktailz.cocktailzclean.service;

import com.cocktailz.cocktailzclean.entity.Cocktail;
import com.cocktailz.cocktailzclean.dto.CocktailDbResponse;
import com.cocktailz.cocktailzclean.repository.CocktailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CocktailImportService {

    private final CocktailRepository cocktailRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public void importCocktailsIfEmpty() {
        if (cocktailRepository.count() > 0) {
            log.info("Cocktails already exist, skipping import.");
            return;
        }

        for (char letter = 'a'; letter <= 'z'; letter++) {
            String url = "https://www.thecocktaildb.com/api/json/v1/1/search.php?f=" + letter;

            try {
                ResponseEntity<CocktailDbResponse> response =
                        restTemplate.getForEntity(url, CocktailDbResponse.class);

                if (response.getBody() != null && response.getBody().getDrinks() != null) {
                    for (CocktailDbResponse.Drink drink : response.getBody().getDrinks()) {
                        String drinkName = drink.getStrDrink();
                        Optional<Cocktail> existing = cocktailRepository.findByName(drinkName);

                        if (existing.isPresent()) continue;

                        Cocktail cocktail = new Cocktail();

                        // Optional: keep original DB ID
                        cocktail.setExternalId(drink.getIdDrink());

                        cocktail.setName(drinkName);
                        cocktail.setInstructions(truncate(drink.getStrInstructions(), 2048));
                        cocktail.setImageUrl(truncate(drink.getStrDrinkThumb(), 1024));
                        cocktail.setCategory(drink.getStrCategory());
                        cocktail.setGlass(drink.getStrGlass());

                        // Collect ingredients dynamically (strIngredient1..15)
                        StringBuilder ingredients = new StringBuilder();
                        try {
                            for (int i = 1; i <= 15; i++) {
                                Method method = drink.getClass()
                                        .getMethod("getStrIngredient" + i);
                                String ingredient = (String) method.invoke(drink);
                                if (ingredient != null && !ingredient.isBlank()) {
                                    ingredients.append(ingredient).append(", ");
                                }
                            }
                        } catch (Exception e) {
                            log.warn("Error while collecting ingredients for {}: {}", drinkName, e.getMessage());
                        }

                        if (!ingredients.isEmpty()) {
                            cocktail.setIngredient(
                                    ingredients.substring(0, ingredients.length() - 2)
                            );
                        }

                        // Alcoholic boolean
                        String alcoholicStr = drink.getStrAlcoholic();
                        Boolean alcoholicBool = null;
                        if (alcoholicStr != null) {
                            if (alcoholicStr.equalsIgnoreCase("Alcoholic")) {
                                alcoholicBool = true;
                            } else if (alcoholicStr.equalsIgnoreCase("Non alcoholic")) {
                                alcoholicBool = false;
                            }
                        }
                        cocktail.setAlcoholic(alcoholicBool);

                        // Save to DB (Long ID will be auto-generated)
                        cocktailRepository.save(cocktail);
                    }
                }
            } catch (Exception e) {
                log.error("Failed to import cocktails for letter {}: {}", letter, e.getMessage(), e);
            }
        }
    }

    private String truncate(String input, int maxLength) {
        if (input == null) return null;
        return input.length() <= maxLength ? input : input.substring(0, maxLength);
    }
}
