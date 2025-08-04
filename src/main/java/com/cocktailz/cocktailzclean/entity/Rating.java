package com.cocktailz.cocktailzclean.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1)
    @Max(10)
    private int score;

    @ManyToOne
    @JoinColumn(name = "cocktail_id") // ✅ fixed column name
    @JsonBackReference
    private Cocktail cocktail;

    @ManyToOne
    @JoinColumn(name = "user_id") // ✅ fixed column name
    @JsonIgnore // ✅ prevent recursion through user
    private User user;
}
