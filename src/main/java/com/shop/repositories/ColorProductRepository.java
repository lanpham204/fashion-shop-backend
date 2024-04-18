package com.shop.repositories;

import com.shop.models.Color;
import com.shop.models.ColorProduct;
import com.shop.models.ColorProductId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ColorProductRepository extends JpaRepository<ColorProduct, ColorProductId> {
    List<ColorProduct> findByProductId(int productId);
}
