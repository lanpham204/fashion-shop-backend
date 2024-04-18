package com.shop.repositories;

import com.shop.models.ProductImage;
import com.shop.models.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SizeRepository extends JpaRepository<Size, Integer> {
    @Query("select s from sizes s where s.id in :ids")
    List<Size> findByIds(@Param("ids") List<Integer> ids);

}
