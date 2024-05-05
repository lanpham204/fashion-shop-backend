package com.shop.services;

import com.shop.dtos.ProductImageDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.Product;
import com.shop.models.ProductImage;
import com.shop.repositories.ProductImageRepository;
import com.shop.repositories.ProductRepository;
import com.shop.response.ProductImageResponse;
import com.shop.services.interfaces.IProductImageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageService implements IProductImageService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProductImageResponse create(ProductImageDTO productImageDTO) throws DataNotFoundException {
        Product product = productRepository.findById(productImageDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot found product with id :" + productImageDTO.getProductId()));
        modelMapper.typeMap(ProductImageDTO.class, ProductImage.class)
                .addMappings(mapper -> mapper.skip(ProductImage::setId));
        ProductImage productImage = modelMapper.map(productImageDTO, ProductImage.class);
        List<ProductImage> productImages = productImageRepository.findByProductId(product.getId());
        if(productImages.get(0).getImageUrl().equals(productImage.getImageUrl())) {
            product.setThumbnail(productImageDTO.getImageUrl());
            productRepository.save(product);
        }
        productImageRepository.save(productImage);
        return modelMapper.map(productImage, ProductImageResponse.class);
    }

    @Override
    public List<ProductImageResponse> getAll() {
        return null;
    }
    @Override
    public List<ProductImageResponse> getByProductId(int productId) {
        return productImageRepository.findByProductId(productId).stream().map(productImage ->
                modelMapper.map(productImage,ProductImageResponse.class)).toList();
    }

    @Override
    public ProductImageResponse update(ProductImageDTO productImageDTO, int id) throws DataNotFoundException {
        Product product = productRepository.findById(productImageDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot found product with id :" + productImageDTO.getProductId()));
        ProductImage productImage = productImageRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot found product image with id :" + id));
        List<ProductImage> productImages = productImageRepository.findByProductId(product.getId());
        if(productImages.get(0).getImageUrl().equals(productImage.getImageUrl())) {
            product.setThumbnail(productImageDTO.getImageUrl());
            productRepository.save(product);
        }
        productImage.setImageUrl(productImageDTO.getImageUrl());
        productImageRepository.save(productImage);

        return modelMapper.map(productImage, ProductImageResponse.class);
    }

    @Override
    public ProductImage getById(int id) throws DataNotFoundException {
        return productImageRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot found product image with id :" + id));
    }

    @Override
    public void delete(int id) throws DataNotFoundException {
        ProductImage productImage = productImageRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot found product image with id :" + id));
        productImageRepository.delete(productImage);
    }
}
