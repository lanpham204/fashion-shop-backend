package com.shop.repositories;

import com.shop.models.ColorProduct;
import com.shop.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(int userId);
    @Query("SELECT o FROM orders o WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR o.user.fullName LIKE %:keyword% OR o.address LIKE %:keyword% " +
            "OR o.note LIKE %:keyword%) order by o.id")
    Page<Order> searchOrderByKeyword(String keyword, Pageable pageable);
}
