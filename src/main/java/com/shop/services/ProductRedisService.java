package com.shop.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.response.ProductListResponse;
import com.shop.response.ProductResponse;
import com.shop.services.interfaces.IProductRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductRedisService implements IProductRedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper redisObjectMapper;

    private String getKeyFrom(int cateId, String keyword, PageRequest pageRequest) {
        int pageNumber = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();
        return String.format("all_product:%d%d:%d:%s",pageNumber,pageSize,cateId,keyword);
    }

    @Override
    public void clear() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Override
    public ProductListResponse getAllProducts(int cateId, String keyword, PageRequest pageRequest) throws JsonProcessingException {
        String key = getKeyFrom(cateId,keyword,pageRequest);
        String json = (String) redisTemplate.opsForValue().get(key);
        ProductListResponse productResponses = json != null?
                redisObjectMapper.readValue(json, new TypeReference<ProductListResponse>() {}): null;
        return productResponses;
    }

    @Override
    public void saveAllProducts(ProductListResponse  productListResponses, int cateId, String keyword, PageRequest pageRequest) throws JsonProcessingException {
        String key = getKeyFrom(cateId,keyword,pageRequest);
        String json = redisObjectMapper.writeValueAsString(productListResponses);
        redisTemplate.opsForValue().set(key,json);

    }

}
