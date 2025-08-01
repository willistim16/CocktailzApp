package com.cocktailz.cocktailzclean;

import com.cocktailz.cocktailzclean.entity.Role;
import com.cocktailz.cocktailzclean.entity.User;
import com.cocktailz.cocktailzclean.repository.RoleRepository;
import com.cocktailz.cocktailzclean.repository.UserRepository;
import com.cocktailz.cocktailzclean.service.CocktailImportService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.metamodel.Metamodel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.cocktailz.cocktailzclean"})
@EntityScan(basePackages = "com.cocktailz.cocktailzclean.entity")
@EnableJpaRepositories(basePackages = "com.cocktailz.cocktailzclean.repository")
public class CocktailzCleanApplication {
    public static void main(String[] args) {
        SpringApplication.run(CocktailzCleanApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            System.out.println("Aantal users in DB: " + userRepository.count());

            // Eerst check of de rol al bestaat, anders aanmaken en opslaan
            String roleName = "ROLE_USER";
            Role role = roleRepository.findByName(roleName)
                    .orElseGet(() -> roleRepository.save(new Role(null, roleName)));

            User user = new User();
            user.setUsername("testuser");
            user.setEmail("test@example.com");
            user.setPassword("secret");
            user.setRole(role);  // gekoppelde, persistent Role

            userRepository.save(user);
            System.out.println("Nieuwe user opgeslagen!");
        };
    }

    @Bean
    CommandLineRunner logEntities(EntityManagerFactory entityManagerFactory) {
        return args -> {
            Metamodel metamodel = entityManagerFactory.getMetamodel();
            System.out.println("Gevonden JPA Entities:");
            metamodel.getEntities().forEach(entityType ->
                    System.out.println(" - " + entityType.getName())
            );
        };
    }

    @Bean
    CommandLineRunner runImporter(CocktailImportService importer) {
        return args -> {
            importer.importCocktails();
        };
    }
}
