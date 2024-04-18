        package com.shop.models;

import com.shop.models.Product;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity(name = "size_products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SizeProduct implements Serializable {
    @EmbeddedId
    private SizeProductId id;

    @ManyToOne
    @JoinColumn(name = "product_id",insertable = false,updatable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "size_id",insertable = false,updatable = false)
    private Size size;


}

