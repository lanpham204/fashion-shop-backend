package com.shop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class ProductDTO {
    @JsonProperty("name")
    @NotEmpty(message = "Product's name cannot be empty")
    @Size(min = 3, max = 200, message = "Product's name must be between 3 and 200 characters")
    private String name;
    @JsonProperty("price")
    @Min(value =  0, message = "total_money must be greater than or equal to 0")
    @Max(value =  100000000, message = "total_money must be less than or equal to 100000000")
    private BigDecimal price;

    @JsonProperty("description")
    private String description;

    @JsonProperty("cate_id")
    @Min(value =  0, message = "cateId must be greater than 0")
    private int cateId;

    @JsonProperty("sizes")
    private List<Integer> sizes;

    @JsonProperty("colors")
    private List<Integer> colors;
}
