package com.cocktailz.cocktailzclean.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Table(name = "cocktail")
@NoArgsConstructor
@AllArgsConstructor
public class Cocktail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String name;

    private Boolean alcoholic;

    // Use TEXT instead of fixed 2048 limit
    @Column(columnDefinition = "TEXT")
    private String instructions;

    // Optionally allow longer image URLs as well
    @Column(length = 2048)
    private String imageUrl;

    @OneToMany(mappedBy = "cocktail", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Rating> ratings;
}
