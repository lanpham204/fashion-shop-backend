package com.shop.repositories;

import com.shop.models.SizeProduct;
import com.shop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("SELECT count(u.id) FROM users u")
    List<Object> statisticalCountUser();
}
