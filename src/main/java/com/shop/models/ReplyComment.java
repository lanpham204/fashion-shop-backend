package com.shop.models;


import jakarta.persistence.*;
import lombok.*;

@Entity(name = "reply_comments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReplyComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "rating_id")
    private Rating rating;

    private String comment;
}

