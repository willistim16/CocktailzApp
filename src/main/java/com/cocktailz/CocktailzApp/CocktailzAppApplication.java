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
import org.springframework.security.crypto.password.PasswordEncoder;

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
    public CommandLineRunner initialUserSetup(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_USER")));

            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN")));

            Role moderatorRole = roleRepository.findByName("ROLE_MODERATOR")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_MODERATOR")));

            if (!userRepository.existsByEmail("admin@example.com")) {
                User admin = new User();
                admin.setUsername("admin_user");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("Admin123!"));
                admin.setRole(adminRole);
                userRepository.save(admin);
                System.out.println("✅ admin_user aangemaakt");
            }

            if (!userRepository.existsByEmail("test_user@example.com")) {
                User user = new User();
                user.setUsername("test_user");
                user.setEmail("test_user@example.com");
                user.setPassword(passwordEncoder.encode("User123!"));
                user.setRole(userRole);
                userRepository.save(user);
                System.out.println("✅ test_user aangemaakt");
            }

            if (!userRepository.existsByEmail("moderator@example.com")) {
                User moderator = new User();
                moderator.setUsername("moderator_user");
                moderator.setEmail("moderator@example.com");
                moderator.setPassword(passwordEncoder.encode("Moderator123!"));
                moderator.setRole(moderatorRole);
                userRepository.save(moderator);
                System.out.println("✅ moderator_user aangemaakt");
            }
        };
    }

}
