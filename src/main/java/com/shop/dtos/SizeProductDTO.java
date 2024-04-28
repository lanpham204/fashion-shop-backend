package com.shop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.ManyToOne;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class SizeProductDTO {
    @JsonProperty("product_id")
    private int productId;

    @ManyToOne
    @JsonProperty("size_id")
    private int sizeId;
}
