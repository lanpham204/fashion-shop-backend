package com.shop.services;

import com.shop.dtos.ProductDTO;
import com.shop.dtos.ProductImageDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.*;
import com.shop.repositories.*;
import com.shop.response.ProductImageResponse;
import com.shop.response.ProductResponse;
import com.shop.response.SizeProductResponse;
import com.shop.services.interfaces.IProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final SizeProductRepository sizeProductRepository;
    private final ColorProductRepository colorProductRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;

    private final ModelMapper modelMapper;
    @Override
    public ProductResponse create(ProductDTO productDTO) throws DataNotFoundException {
        Category category = categoryRepository.findById(productDTO.getCateId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find category with id: " + productDTO.getCateId() ));
        modelMapper.typeMap(ProductDTO.class, Product.class)
                .addMappings(mapper -> mapper.skip(Product::setId));
        Product product = modelMapper.map(productDTO, Product.class);
        product.setCategory(category);
        Product productSave = productRepository.save(product);
        List<Size> sizes = sizeRepository.findByIds(productDTO.getSizes());
        List<SizeProduct> sizeProducts = new ArrayList<>();
        sizes.stream().forEach(size -> sizeProducts.add(
                        new SizeProduct(new SizeProductId(productSave.getId(), size.getId()), productSave, size)));
        sizeProductRepository.saveAll(sizeProducts);
        List<Color> colors = colorRepository.findByIds(productDTO.getColors());
        List<ColorProduct> colorProducts = new ArrayList<>();
        colors.stream().forEach(color -> colorProducts.add(
                new ColorProduct(new ColorProductId(productSave.getId(), color.getId()), productSave, color)));
        colorProductRepository.saveAll(colorProducts);
        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
        productResponse.setColors(colors);
        productResponse.setSizes(sizes);
        return productResponse;
    }

    @Override
    public Page<ProductResponse> searchProducts(int cateId, String keyword, PageRequest pageRequest) {
        return productRepository.searchProducts(cateId,keyword,pageRequest)
                .map(product -> {
                    try {
                        return getProductDetailById(product.getId());
                    } catch (DataNotFoundException e) {
                        return null;
                    }
                });
    }
    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }
    @Override
    public ProductResponse update(ProductDTO productDTO, int id) throws DataNotFoundException {
        Category category = categoryRepository.findById(productDTO.getCateId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find category with id: " + productDTO.getCateId() ));
        Product product = getById(id);
        product = modelMapper.map(productDTO, Product.class);
        productRepository.save(product);
        return modelMapper.map(product, ProductResponse.class);
    }

    @Override
    public Product getById(int id) throws DataNotFoundException {
        return productRepository.findById(id).orElseThrow(()-> new DataNotFoundException("Cannot find product with id: " + id));
    }

    @Override
    public ProductResponse getProductDetailById(int id) throws DataNotFoundException {
        Product product = getById(id);
        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
        productResponse.setCateId(product.getCategory().getId());
        List<ProductImageResponse> productImages = productImageRepository.findByProductId(id)
                .stream().map(productImage -> modelMapper.map(productImage,ProductImageResponse.class)).toList();
        List<SizeProduct> sizeProducts = sizeProductRepository.findByProductId(id);
        List<Integer> sizeIds = new ArrayList<>();
        sizeProducts.stream().forEach(sizeProduct -> sizeIds.add(sizeProduct.getSize().getId()));
        List<Size> sizes = sizeRepository.findByIds(sizeIds);
        List<ColorProduct> colorProducts = colorProductRepository.findByProductId(id);
        List<Integer> colorIds = new ArrayList<>();
        colorProducts.stream().forEach(colorProduct -> colorIds.add(colorProduct.getColor().getId()));
        List<Color> colors = colorRepository.findByIds(colorIds);
        productResponse.setProductImages(productImages);
        productResponse.setSizes(sizes);
        productResponse.setColors(colors);
        return productResponse;
    }
    @Override
    public List<ProductResponse> getProductDetailsByIds(List<Integer> productIds)  {
        List<ProductResponse> products = productRepository.findProductByIds(productIds).stream()
                .map(product -> {
                    try {
                        return getProductDetailById(product.getId());
                    } catch (DataNotFoundException e) {
                        return null;
                    }
                }).toList();
        return products;
    }
    @Override
    public void delete(int id) throws DataNotFoundException {
        Product product = getById(id);
        productRepository.delete(product);
    }

    @Override
    public ProductImage createProductImage(int productId, ProductImageDTO productImageDTO) throws DataNotFoundException {
        Product product = getById(productId);
        List<ProductImage> productImages = productImageRepository.findByProductId(productId);
        if(productImages.size() >0) {
            product.setThumbnail(productImages.get(0).getImageUrl());
        }
        ProductImage productImage = ProductImage.builder()
                .product(product)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        return productImageRepository.save(productImage);
    }
}
