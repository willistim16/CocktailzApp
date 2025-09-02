package com.cocktailz.cocktailzclean.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "note")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content; // previously "content" -> use getContent()

    @ManyToOne
    @JoinColumn(name = "favorite_id")
    @JsonBackReference(value = "favorite-notes")
    private Favorite favorite;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-notes")
    private User user;
}
