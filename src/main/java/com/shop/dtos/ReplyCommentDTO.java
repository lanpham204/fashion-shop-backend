package com.shop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class ReplyCommentDTO {
    @JsonProperty("rating_id")
    @Min(value = 1, message = "Rating_id must be > 0")
    private Integer ratingId;
    @NotBlank(message = "Comment is required")
    private String comment;
}
