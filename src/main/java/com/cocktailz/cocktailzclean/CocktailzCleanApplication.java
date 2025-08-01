package com.cocktailz.cocktailzclean;

import com.cocktailz.cocktailzclean.Entity.Role;
import com.cocktailz.cocktailzclean.Entity.User;
import com.cocktailz.cocktailzclean.repository.CocktailRepository;
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
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EntityScan(basePackages = "com.cocktailz.cocktailzclean.Entity")
@EnableJpaRepositories(basePackages = "com.cocktailz.cocktailzclean.repository")
public class CocktailzCleanApplication {
    public static void main(String[] args) {
        SpringApplication.run(CocktailzCleanApplication.class, args);
    }

    @Bean
    CommandLineRunner logEntities(EntityManagerFactory entityManagerFactory) {
        return args -> {
            Metamodel metamodel = entityManagerFactory.getMetamodel();
            System.out.println("📦 Gevonden JPA Entities:");
            metamodel.getEntities().forEach(entityType ->
                    System.out.println(" - " + entityType.getName())
            );
        };
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    CommandLineRunner runImporter(CocktailImportService importer) {
        return args -> {
            importer.importCocktails();
        };
    }

    @Bean
    CommandLineRunner testRepo(UserRepository userRepository) {
        return args -> {
            System.out.println("👥 Aantal users in DB: " + userRepository.count());
        };
    }

    @Bean
    CommandLineRunner testCocktails(CocktailRepository cocktailRepository) {
        return args -> {
            System.out.println("Total cocktails in DB: " + cocktailRepository.count());
            cocktailRepository.findAll().forEach(c -> System.out.println(c.getName()));
        };
    }


    @Bean
    public CommandLineRunner initialUserSetup(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            // Setup ROLE_USER
            Role userRole = roleRepository.findByName("USER");
            if (userRole == null) {
                userRole = new Role("USER");
                roleRepository.save(userRole);
                System.out.println("✅ Role 'USER' created");
            }

            // Setup ROLE_ADMIN
            Role adminRole = roleRepository.findByName("ADMIN");
            if (adminRole == null) {
                adminRole = new Role("ADMIN");
                roleRepository.save(adminRole);
                System.out.println("✅ Role 'ADMIN' created");
            }

            // Setup test admin user
            if (!userRepository.existsByEmail("admin@example.com")) {
                User adminUser = new User();
                adminUser.setUsername("admin");
                adminUser.setEmail("admin@example.com");
                adminUser.setPassword("hashedPassword"); // Replace with real hash
                adminUser.setRole(adminRole);
                userRepository.save(adminUser);
                System.out.println("✅ Admin user created");
            }

            // Setup regular user
            if (!userRepository.existsByEmail("user1@example.com")) {
                User regularUser = new User();
                regularUser.setUsername("user1");
                regularUser.setEmail("user1@example.com");
                regularUser.setPassword("encryptedPassword"); // Replace with real hash
                regularUser.setRole(userRole);
                userRepository.save(regularUser);
                System.out.println("✅ Regular user created");
            }
        };
    }
}
