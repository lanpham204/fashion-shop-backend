package com.shop.models;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "product_images")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductImage {
    public static final int MAXIMUM_IMAGES_SIZE = 5;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}