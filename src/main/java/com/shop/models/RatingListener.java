package com.shop.models;

import com.shop.services.interfaces.IProductRedisService;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RatingListener {
    private final IProductRedisService productRedisService;

    @PostPersist
    public void postPersist(Rating rating) {
        productRedisService.clear();
    }
    @PostUpdate
    public void postUpdate(Rating rating) {
        productRedisService.clear();
    }
    @PostRemove
    public void postRemove(Rating rating) {
        productRedisService.clear();
    }
}
