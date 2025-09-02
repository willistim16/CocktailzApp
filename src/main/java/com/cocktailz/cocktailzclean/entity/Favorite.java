package com.cocktailz.cocktailzclean.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "favorite",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "cocktail_id"}))

public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference(value = "user-favorites")
    private User user;

    @ManyToOne
    @JoinColumn(name = "cocktail_id", nullable = false)
    private Cocktail cocktail;

    @OneToMany(mappedBy = "favorite", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "favorite-notes")
    @Builder.Default
    private List<Note> notes = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private Rating rating;
}
