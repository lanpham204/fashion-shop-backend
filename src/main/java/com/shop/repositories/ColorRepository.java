package com.shop.repositories;

import com.shop.models.Color;
import com.shop.models.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ColorRepository extends JpaRepository<Color, Integer> {
    @Query("select c from colors c where c.id in :ids")
    List<Color> findByIds(@Param("ids") List<Integer> ids);
}
