package com.shop.models;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "sizes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Size {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "size", nullable = false)
    private String size;

}
