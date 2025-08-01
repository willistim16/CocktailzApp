package com.cocktailz.cocktailzclean.service;

import com.cocktailz.cocktailzclean.Entity.Cocktail;
import com.cocktailz.cocktailzclean.dto.CocktailDbResponse;
import com.cocktailz.cocktailzclean.repository.CocktailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CocktailImportService {

    private final CocktailRepository cocktailRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public void importCocktails() {
        System.out.println("🚀 importCocktails() gestart");

        if (cocktailRepository.count() > 0) {
            System.out.println("📦 Cocktail data al aanwezig, import wordt overgeslagen.");
            return;
        }

        int totalImported = 0;

        for (char letter = 'a'; letter <= 'z'; letter++) {
            String url = "https://www.thecocktaildb.com/api/json/v1/1/search.php?f=" + letter;

            try {
                ResponseEntity<CocktailDbResponse> response = restTemplate.getForEntity(url, CocktailDbResponse.class);

                if (response.getBody() != null && response.getBody().getDrinks() != null) {
                    for (CocktailDbResponse.Drink drink : response.getBody().getDrinks()) {
                        Cocktail cocktail = new Cocktail();
                        cocktail.setName(drink.getStrDrink());
                        cocktail.setInstructions(drink.getStrInstructions());
                        cocktail.setImageUrl(drink.getStrDrinkThumb());
                        cocktail.setAlcoholic("Alcoholic".equalsIgnoreCase(drink.getStrAlcoholic()));

                        cocktailRepository.save(cocktail);
                        totalImported++;
                        System.out.println("✅ Opgeslagen: " + cocktail.getName());
                    }
                } else {
                    System.out.println("⚠️ Geen data gevonden voor letter: " + letter);
                }
            } catch (Exception e) {
                System.err.println("❌ Fout bij ophalen van letter '" + letter + "': " + e.getMessage());
            }
        }

        System.out.println("✅ Totaal aantal geïmporteerde cocktails: " + totalImported);
    }
}
