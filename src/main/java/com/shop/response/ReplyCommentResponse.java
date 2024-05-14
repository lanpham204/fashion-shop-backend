package com.shop.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop.models.Rating;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReplyCommentResponse {
    private Long id;

    @JsonProperty("rating_id")
    private int ratingId;

    private String comment;
}

