package com.cocktailz.cocktailzclean.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    @Id @GeneratedValue
    private Long id;

    private String content;
}