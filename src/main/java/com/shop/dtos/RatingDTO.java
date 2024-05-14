package com.shop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class RatingDTO {
    @JsonProperty("product_id")
    @Min(value = 1, message = "Product_id must be > 0")
    private int productId;
    @JsonProperty("user_id")
    @Min(value = 1, message = "User_id must be > 0")
    private int userId;
    @Min(value = 1, message = "Value must be > 0")
    @Max(value = 5, message = "Value must be < 5")
    private Integer value;
    @NotBlank(message = "Comment is required")
    private String comment;

}
