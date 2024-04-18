package com.shop.repositories;

import com.shop.models.Size;
import com.shop.models.SizeProduct;
import com.shop.models.SizeProductId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SizeProductRepository extends JpaRepository<SizeProduct, SizeProductId> {
    List<SizeProduct> findByProductId(int productId);


}
