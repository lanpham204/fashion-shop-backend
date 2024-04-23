package com.shop.services.interfaces;

import com.shop.dtos.ProductImageDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.ProductImage;
import com.shop.response.ProductImageResponse;

import java.util.List;

public interface IProductImageService {
    ProductImageResponse create(ProductImageDTO productImageDTO) throws DataNotFoundException;
    List<ProductImageResponse> getAll();
    ProductImageResponse update(ProductImageDTO productImageDTO,int id) throws DataNotFoundException;
    ProductImage getById(int id) throws DataNotFoundException;
    void delete(int id) throws DataNotFoundException;
}
