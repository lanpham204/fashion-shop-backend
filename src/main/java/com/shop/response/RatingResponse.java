package com.shop.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop.models.ReplyComment;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RatingResponse {

    private int id;

    @JsonProperty("product_id")
    private int productId;

    @JsonProperty("user")
    private UserResponse user;

    private Integer value;

    private String comment;
    @JsonProperty("reply_comments")
    private List<ReplyCommentResponse> replyComments;
}

