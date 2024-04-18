package com.shop.repositories;

import com.shop.models.Category;
import com.shop.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
