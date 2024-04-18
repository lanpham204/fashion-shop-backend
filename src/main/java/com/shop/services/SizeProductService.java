package com.shop.services;

import com.shop.response.SizeProductResponse;
import com.shop.services.interfaces.ISizeProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SizeProductService implements ISizeProductService {
    @Override
    public SizeProductResponse create(SizeProductResponse sizeProductResponse) {
        return null;
    }
}
