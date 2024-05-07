package com.shop.models;

import com.shop.services.interfaces.IProductRedisService;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

@RequiredArgsConstructor
public class ProductListener {
    private final IProductRedisService productRedisService;

    @PostPersist
    public void postPersist(Product product) {
        productRedisService.clear();
    }
    @PostUpdate
    public void postUpdate(Product product) {
        productRedisService.clear();
    }
    @PostRemove
    public void postRemove(Product product) {
        productRedisService.clear();
    }
}
