package com.shop.repositories;

import com.shop.models.OrderDetail;
import com.shop.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p from products p where " +
            "(:cate_id is null or :cate_id = 0 or p.category.id = :cate_id) "
            +"and (:keyword is null or :keyword = '' or p.name like %:keyword% or p.description like %:keyword%) " +
            "order by p.id desc ")
    Page<Product> searchProducts(@Param("cate_id") int cate_id,
                                 @Param("keyword") String keyword,
                                 Pageable pageable);
    @Query("select p from products p where p.id in :ids")
    List<Product> findProductByIds(@Param("ids") List<Integer> ids);
}
