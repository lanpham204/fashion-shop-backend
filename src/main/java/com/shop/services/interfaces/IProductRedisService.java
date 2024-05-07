package com.shop.services.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shop.response.ProductListResponse;
import com.shop.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductRedisService {
    void clear();
    ProductListResponse getAllProducts(int cateId, String keyword, PageRequest pageRequest) throws JsonProcessingException;
    void saveAllProducts(ProductListResponse  productListResponses,int cateId, String keyword, PageRequest pageRequest) throws JsonProcessingException;

}
