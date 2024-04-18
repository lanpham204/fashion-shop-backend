package com.shop.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ColorProductId implements Serializable {
    @Column(name = "product_id")
    private int productId;
    @Column(name = "color_id")
    private int colorId;


}
