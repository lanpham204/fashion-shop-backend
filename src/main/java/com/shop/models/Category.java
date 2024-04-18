package com.shop.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "categories") // Specifying table name for clarity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

}
