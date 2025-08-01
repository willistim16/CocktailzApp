package com.cocktailz.cocktailzclean.service;

import com.cocktailz.cocktailzclean.Entity.Cocktail;
import com.cocktailz.cocktailzclean.dto.CocktailDbResponse;
import com.cocktailz.cocktailzclean.repository.CocktailRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CocktailImportService {

    private final CocktailRepository cocktailRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public CocktailImportService(CocktailRepository cocktailRepository) {
        this.cocktailRepository = cocktailRepository;
    }

    public void importCocktails() {

        System.out.println("🚀 importCocktails() gestart");

        if (cocktailRepository.count() > 0) {
            System.out.println("Cocktail data al aanwezig, import wordt overgeslagen.");
            return;
        }

        String url = "https://www.thecocktaildb.com/api/json/v1/1/search.php?f=a";
        CocktailDbResponse response = restTemplate.getForObject(url, CocktailDbResponse.class);

        if (response != null && response.getDrinks() != null) {
            response.getDrinks().forEach(drink -> {
                Cocktail cocktail = new Cocktail();
                cocktail.setName(drink.getStrDrink());
                cocktail.setInstructions(drink.getStrInstructions());
                cocktail.setImageUrl(drink.getStrDrinkThumb());
                cocktail.setAlcoholic("Alcoholic".equalsIgnoreCase(drink.getStrAlcoholic()));
                cocktailRepository.save(cocktail);
                System.out.println("Cocktail opgeslagen: " + cocktail.getName());
            });
        } else {
            System.out.println("Geen cocktails opgehaald van externe API.");
        }
    }
}
