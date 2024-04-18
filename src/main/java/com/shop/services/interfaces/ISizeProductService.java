package com.shop.services.interfaces;

import com.shop.dtos.ColorProductDTO;
import com.shop.repositories.ColorProductRepository;
import com.shop.response.SizeProductResponse;

public interface ISizeProductService {
    SizeProductResponse create(SizeProductResponse sizeProductResponse);

}
