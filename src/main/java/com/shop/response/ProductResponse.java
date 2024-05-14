package com.shop.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop.models.Category;
import com.shop.models.Color;
import com.shop.models.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("thumbnail")
    private String thumbnail;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("description")
    private String description;

    @JsonProperty("category")
    private Category category;

    @JsonProperty("product_images")
    private List<ProductImageResponse> productImages;
    @JsonProperty("size")
    private List<Size> sizes;
    @JsonProperty("color")
    private List<Color> colors;
    @JsonProperty("ratings")
    private List<RatingResponse> ratings;
}
