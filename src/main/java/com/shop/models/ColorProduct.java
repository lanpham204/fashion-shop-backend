package com.shop.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity(name = "color_products")

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ColorProduct implements Serializable {
    @EmbeddedId
    private ColorProductId id;
    @ManyToOne
    @JoinColumn(name = "product_id",insertable = false,updatable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "color_id",insertable = false,updatable = false)
    private Color color;


}

