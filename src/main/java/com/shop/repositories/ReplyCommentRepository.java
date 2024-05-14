package com.shop.repositories;

import com.shop.models.Rating;
import com.shop.models.ReplyComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyCommentRepository extends JpaRepository<ReplyComment,Integer> {
    List<ReplyComment> findByRatingId(int ratingId);
}
