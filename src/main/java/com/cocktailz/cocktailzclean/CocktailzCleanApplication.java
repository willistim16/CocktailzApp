package com.cocktailz.cocktailzclean;

import com.cocktailz.cocktailzclean.entity.Role;
import com.cocktailz.cocktailzclean.entity.User;
import com.cocktailz.cocktailzclean.repository.RoleRepository;
import com.cocktailz.cocktailzclean.repository.UserRepository;
import com.cocktailz.cocktailzclean.service.CocktailImportService;
//import jakarta.persistence.EntityManagerFactory;
//import jakarta.persistence.metamodel.Metamodel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.cocktailz.cocktailzclean.entity") // ✅ Correct spelling (geen hoofdletter E)
@EnableJpaRepositories(basePackages = "com.cocktailz.cocktailzclean.repository")
public class CocktailzCleanApplication {
    public static void main(String[] args) {
        SpringApplication.run(CocktailzCleanApplication.class, args);
    }

//    @Bean
//    CommandLineRunner logEntities(EntityManagerFactory entityManagerFactory) {
//        return args -> {
//            Metamodel metamodel = entityManagerFactory.getMetamodel();
//            System.out.println("📦 Gevonden JPA Entities:");
//            metamodel.getEntities().forEach(entityType ->
//                    System.out.println(" - " + entityType.getName())
//            );
//        };
//    }

    @Bean
    CommandLineRunner runImporter(CocktailImportService importer) {
        return args -> {
            importer.importCocktailsIfEmpty();
        };
    }

    @Bean
    CommandLineRunner testRepo(UserRepository userRepository) {
        return args -> {
            System.out.println("👥 Users in DB: " + userRepository.findAll().size());
        };
    }

    @Bean
    public CommandLineRunner initialUserSetup(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            // 🛡️ Zorg dat de rollen bestaan
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_USER")));

            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN")));

            // 👤 testuser toevoegen als hij nog niet bestaat
            if (!userRepository.existsByEmail("test@example.com")) {
                User user = new User();
                user.setUsername("testuser");
                user.setEmail("test@example.com");
                user.setPassword("secret"); // ➕ vervang dit later door gehashte versie
                user.setRole(userRole);
                userRepository.save(user);
                System.out.println("✅ testuser aangemaakt");
            }

            // 👤 admin toevoegen als hij nog niet bestaat
            if (!userRepository.existsByEmail("admin@example.com")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword("hashedPassword"); // ➕ hash dit later
                admin.setRole(adminRole);
                userRepository.save(admin);
                System.out.println("✅ admin aangemaakt");
            }
        };
    }
}
