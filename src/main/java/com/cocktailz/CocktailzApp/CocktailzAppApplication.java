package com.cocktailz.CocktailzApp;

import com.cocktailz.CocktailzApp.entity.Role;
import com.cocktailz.CocktailzApp.entity.User;
import com.cocktailz.CocktailzApp.repository.RoleRepository;
import com.cocktailz.CocktailzApp.repository.UserRepository;
import com.cocktailz.CocktailzApp.service.CocktailImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication
@EntityScan(basePackages = "com.cocktailz.CocktailzApp.entity")
@EnableJpaRepositories(basePackages = "com.cocktailz.CocktailzApp.repository")
public class CocktailzAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CocktailzAppApplication.class, args);
    }

    @Bean
    public CommandLineRunner importCocktails(CocktailImportService importService) {
        return args -> {
            System.out.println("🔄 Checking for existing cocktails...");
            importService.importCocktailsIfEmpty();
            System.out.println("✅ Cocktail import completed.");
        };
    }

    @Bean
    public CommandLineRunner testRepo(UserRepository userRepository) {
        return args -> {
            System.out.println("👥 Users in DB: " + userRepository.findAll().size());
        };
    }

    @Bean
    public CommandLineRunner testDb(DataSource dataSource) {
        return args -> {
            try (Connection conn = dataSource.getConnection()) {
                System.out.println("Connected to DB: " + conn.getCatalog());
            }
        };
    }

    @Bean
    public CommandLineRunner initialUserSetup(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_USER")));

            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN")));

            if (!userRepository.existsByEmail("test@example.com")) {
                User user = new User();
                user.setUsername("testuser");
                user.setEmail("test@example.com");
                user.setPassword("secret");
                user.setRole(userRole);
                userRepository.save(user);
                System.out.println("✅ testuser aangemaakt");
            }

            if (!userRepository.existsByEmail("admin@example.com")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword("hashedPassword");
                admin.setRole(adminRole);
                userRepository.save(admin);
                System.out.println("✅ admin aangemaakt");
            }
        };
    }
}
