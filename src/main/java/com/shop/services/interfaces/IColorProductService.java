package com.shop.services.interfaces;

import com.shop.dtos.ColorProductDTO;
import com.shop.repositories.ColorProductRepository;

public interface IColorProductService {
    ColorProductRepository create(ColorProductDTO colorProductDTO);
}
