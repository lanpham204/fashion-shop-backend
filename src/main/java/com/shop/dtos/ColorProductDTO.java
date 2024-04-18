package com.shop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop.models.Color;
import com.shop.models.Product;
import jakarta.persistence.ManyToOne;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class ColorProductDTO {
    @JsonProperty("product_id")
    private Product product;

    @ManyToOne
    @JsonProperty("color_id")
    private Color color;
}
