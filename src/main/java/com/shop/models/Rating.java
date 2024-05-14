package com.shop.models;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "ratings")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(RatingListener.class)

public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "product_id" )
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id" )
    private User user;

    private Integer value;

    private String comment;
    @OneToMany(mappedBy = "rating", cascade = CascadeType.ALL)
    private List<ReplyComment> replyComments;
}

