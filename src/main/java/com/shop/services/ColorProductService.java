package com.shop.services;

import com.shop.dtos.ColorProductDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.Color;
import com.shop.models.ColorProduct;
import com.shop.models.ColorProductId;
import com.shop.models.Product;
import com.shop.repositories.ColorProductRepository;
import com.shop.repositories.ColorRepository;
import com.shop.repositories.ProductRepository;
import com.shop.services.interfaces.IColorProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorProductService implements IColorProductService {
    private final ColorProductRepository colorProductRepository;
    private final ProductRepository productRepository;
    private final ColorRepository colorRepository;
    @Override
    public ColorProduct create(ColorProductDTO colorProductDTO) throws DataNotFoundException {
        Product product = productRepository.findById(colorProductDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot found product with id :" + colorProductDTO.getProductId()));
        Color color = colorRepository.findById(colorProductDTO.getColorId()).orElseThrow(() ->
                new DataNotFoundException("Cannot found color with id :" + colorProductDTO.getColorId()));
        return colorProductRepository.save(ColorProduct.builder()
                        .id(new ColorProductId(colorProductDTO.getProductId(),colorProductDTO.getColorId()))
                        .product(product)
                        .color(color)
                .build());
    }

    @Override
    public List<ColorProduct> getByProductId(int productId) throws DataNotFoundException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Cannot found product with id :" + productId));
        return colorProductRepository.findByProductId(productId);
    }
    @Override
    public void delete(ColorProductId colorProductId) throws DataNotFoundException {
        ColorProduct colorProduct = colorProductRepository.findById(colorProductId).
                orElseThrow(() -> new DataNotFoundException("Cannot found product with id :" + colorProductId));
        colorProductRepository.delete(colorProduct);
    }
}
