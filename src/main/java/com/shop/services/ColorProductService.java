package com.shop.services;

import com.shop.dtos.ColorProductDTO;
import com.shop.repositories.ColorProductRepository;
import com.shop.services.interfaces.IColorProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ColorProductService implements IColorProductService {
    @Override
    public ColorProductRepository create(ColorProductDTO colorProductDTO) {
        return null;
    }
}
