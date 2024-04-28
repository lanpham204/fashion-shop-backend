package com.shop.services;

import com.shop.dtos.SizeProductDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.Product;
import com.shop.models.Size;
import com.shop.models.SizeProduct;
import com.shop.models.SizeProductId;
import com.shop.repositories.ProductRepository;
import com.shop.repositories.SizeProductRepository;
import com.shop.repositories.SizeRepository;
import com.shop.services.interfaces.ISizeProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SizeProductService implements ISizeProductService {
    private final SizeProductRepository sizeProductRepository;
    private final ProductRepository productRepository;
    private final SizeRepository sizeRepository;

    @Override
    public SizeProduct create(SizeProductDTO sizeProductDTO) throws DataNotFoundException {
        Product product = productRepository.findById(sizeProductDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id :" + sizeProductDTO.getProductId()));

        Size size = sizeRepository.findById(sizeProductDTO.getSizeId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find size with id :" + sizeProductDTO.getSizeId()));
        return sizeProductRepository.save(SizeProduct.builder()
                .id(new SizeProductId(sizeProductDTO.getProductId(), sizeProductDTO.getSizeId()))
                .product(product)
                .size(size)
                .build());
    }

    @Override
    public List<SizeProduct> getByProductId(int productId) throws DataNotFoundException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Cannot found product with id :" + productId));
        return sizeProductRepository.findByProductId(productId);
    }

    @Override
    public void delete(SizeProductId sizeProductId) throws DataNotFoundException {
        SizeProduct sizeProduct = sizeProductRepository.findById(sizeProductId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find size product with id :" + sizeProductId));
        sizeProductRepository.delete(sizeProduct);
    }
}
