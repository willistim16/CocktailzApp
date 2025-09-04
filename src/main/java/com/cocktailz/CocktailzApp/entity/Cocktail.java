package com.cocktailz.CocktailzApp.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cocktail")
public class Cocktail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 50)
    private String externalId;

    private String idDrink;

    @Column(length = 500, nullable = false)
    private String name;

    private String category;

    private String glass;

    private Boolean alcoholic;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    @Column(columnDefinition = "TEXT")
    private String ingredient;

    @Column(length = 2048)
    private String imageUrl;

    @OneToMany(mappedBy = "cocktail", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Rating> ratings;

    public Cocktail(Long id, String idDrink, String name, Boolean alcoholic, String instructions, String imageUrl) {
        this.idDrink = idDrink;
        this.id = id;
        this.name = name;
        this.alcoholic = alcoholic;
        this.instructions = instructions;
        this.imageUrl = imageUrl;
    }

    public Cocktail(String externalId, String name, Boolean alcoholic, String instructions, String imageUrl) {
        this.externalId = externalId;
        this.name = name;
        this.alcoholic = alcoholic;
        this.instructions = instructions;
        this.imageUrl = imageUrl;
    }
}
