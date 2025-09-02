package com.cocktailz.cocktailzclean;

import com.cocktailz.cocktailzclean.entity.Role;
import com.cocktailz.cocktailzclean.entity.User;
import com.cocktailz.cocktailzclean.repository.RoleRepository;
import com.cocktailz.cocktailzclean.repository.UserRepository;
import com.cocktailz.cocktailzclean.service.CocktailImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.cocktailz.cocktailzclean.entity")
@EnableJpaRepositories(basePackages = "com.cocktailz.cocktailzclean.repository")
public class CocktailzCleanApplication {

    private final CocktailImportService importService;

    public CocktailzCleanApplication(CocktailImportService importService) {
        this.importService = importService;
    }

    public static void main(String[] args) {
        SpringApplication.run(CocktailzCleanApplication.class, args);
    }

    /**
     * CommandLineRunner to import cocktails if the database is empty.
     */
    @Bean
    public CommandLineRunner runImporter() {
        return args -> {
            System.out.println("🔄 Checking for existing cocktails...");
            importService.importCocktailsIfEmpty();
            System.out.println("✅ Cocktail import completed.");
        };
    }

    /**
     * CommandLineRunner to check how many users exist in the DB.
     */
    @Bean
    public CommandLineRunner testRepo(UserRepository userRepository) {
        return args -> {
            System.out.println("👥 Users in DB: " + userRepository.findAll().size());
        };
    }

    /**
     * CommandLineRunner to set up initial roles and test users.
     */
    @Bean
    public CommandLineRunner initialUserSetup(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            // Ensure roles exist
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_USER")));

            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN")));

            // Create test user if not exists
            if (!userRepository.existsByEmail("test@example.com")) {
                User user = new User();
                user.setUsername("testuser");
                user.setEmail("test@example.com");
                user.setPassword("secret"); // TODO: replace with hashed password
                user.setRole(userRole);
                userRepository.save(user);
                System.out.println("✅ testuser aangemaakt");
            }

            // Create admin if not exists
            if (!userRepository.existsByEmail("admin@example.com")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword("hashedPassword"); // TODO: replace with hashed password
                admin.setRole(adminRole);
                userRepository.save(admin);
                System.out.println("✅ admin aangemaakt");
            }
        };
    }
}
