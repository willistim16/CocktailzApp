package com.cocktailz.cocktailzclean.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // bijv. "ROLE_USER", "ROLE_ADMIN"

    // Custom constructor to allow new Role("USER")
    public Role(String name) {
        this.name = name;
    }
}
