 package com.shop.models;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "colors") // Specifying table name for clarity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Color {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "color", nullable = false)
    private String color;
}