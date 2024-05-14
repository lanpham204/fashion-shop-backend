package com.shop.repositories;

import com.shop.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating,Integer> {
    List<Rating> findByProductId(int productId);
}
