package com.cocktailz.cocktailzclean.service;

import com.cocktailz.cocktailzclean.entity.Cocktail;
import com.cocktailz.cocktailzclean.dto.CocktailDbResponse;
import com.cocktailz.cocktailzclean.repository.CocktailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CocktailImportService {

    private final CocktailRepository cocktailRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public void importCocktailsIfEmpty() {
        if (cocktailRepository.count() > 0) {
            // Skip import if data already exists
            return;
        }

        for (char letter = 'a'; letter <= 'z'; letter++) {
            String url = "https://www.thecocktaildb.com/api/json/v1/1/search.php?f=" + letter;

            try {
                ResponseEntity<CocktailDbResponse> response = restTemplate.getForEntity(url, CocktailDbResponse.class);

                if (response.getBody() != null && response.getBody().getDrinks() != null) {
                    for (CocktailDbResponse.Drink drink : response.getBody().getDrinks()) {
                        String drinkName = drink.getStrDrink();
                        Optional<Cocktail> existing = cocktailRepository.findByName(drinkName);

                        if (existing.isPresent()) continue;

                        Cocktail cocktail = new Cocktail();
                        cocktail.setName(drinkName);
                        cocktail.setInstructions(truncate(drink.getStrInstructions(), 2048));
                        cocktail.setImageUrl(truncate(drink.getStrDrinkThumb(), 1024));

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

                        cocktailRepository.save(cocktail);
                    }
                }
            } catch (Exception e) {
                // Consider logging if needed
            }
        }
    }

    private String truncate(String input, int maxLength) {
        if (input == null) return null;
        return input.length() <= maxLength ? input : input.substring(0, maxLength);
    }
}
