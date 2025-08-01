package com.cocktailz.cocktailzclean.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cocktail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String alcoholic;
    private String instructions;
    private String imageUrl;

    @OneToMany(mappedBy = "cocktail", cascade = CascadeType.ALL)
    private List<Rating> ratings;
}
