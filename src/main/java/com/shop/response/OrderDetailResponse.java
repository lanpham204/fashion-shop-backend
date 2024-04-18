package com.shop.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop.models.Order;
import com.shop.models.Product;
import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailResponse {
    @JsonProperty("id")
    private int id;
    @JsonProperty("order_id")
    private int orderId;

    @JsonProperty("product")
    private Product product;

    @JsonProperty("size")
    private String size;

    @JsonProperty("color")
    private String color;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("quantity")
    private int quantity ;

    @JsonProperty("total_money")
    private BigDecimal totalMoney;
}
