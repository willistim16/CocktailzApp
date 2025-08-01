package com.cocktailz.cocktailzclean.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Cocktail cocktail;

    @OneToOne(cascade = CascadeType.ALL)
    private Note note;

    @OneToOne(cascade = CascadeType.ALL)
    private Rating rating;
}
