package com.shop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    @JsonProperty("product_id")
    private Integer productId;
    @JsonProperty("size")
    private String size;
    @JsonProperty("color")
    private String color;
    @JsonProperty("quantity")
    private Integer quantity;
    @JsonProperty("price")
    private BigDecimal price;
}