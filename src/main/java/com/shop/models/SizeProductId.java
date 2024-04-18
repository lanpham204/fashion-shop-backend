package com.shop.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class SizeProductId implements Serializable {
    @Column(name = "product_id")
    private int productId;
    @Column(name = "size_id")
    private int sizeId;

}
