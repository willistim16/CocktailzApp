package com.cocktailz.cocktailzclean.Entity;

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

    private boolean alcoholic;

    @Column(columnDefinition = "TEXT") // allows very long text
    private String instructions;

    @Column(length = 1024)
    private String imageUrl;

    @OneToMany(mappedBy = "cocktail", cascade = CascadeType.ALL)
    private List<Rating> ratings;
}
