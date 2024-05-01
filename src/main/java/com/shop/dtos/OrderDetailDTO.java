package com.shop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1, message = "order_id must be > 0")
    private int orderId;

    @JsonProperty("product_id")
    @Min(value = 1, message = "product_id must be > 0")
    private int productId;

    @JsonProperty("size")
    @NotBlank(message = "size is require")
    private String size;

    @JsonProperty("color")
    @NotBlank(message = "color is require")
    private String color;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("price")
    @Min(value =  0, message = "price must be greater than or equal to 0")
    @Max(value =  100000000, message = "price must be less than or equal to 100000000")
    private BigDecimal price;

    @JsonProperty("total_money")
    @Min(value =  0, message = "total_money must be greater than or equal to 0")
    @Max(value =  100000000, message = "total_money must be less than or equal to 100000000")
    private BigDecimal totalMoney;
    @JsonProperty("total_money_order")
    private BigDecimal totalMoneyOrder;
}
