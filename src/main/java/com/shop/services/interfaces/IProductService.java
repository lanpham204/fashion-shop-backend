package com.shop.services.interfaces;

import com.shop.dtos.ProductDTO;
import com.shop.dtos.ProductImageDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.Product;
import com.shop.models.ProductImage;
import com.shop.response.ProductImageResponse;
import com.shop.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductService {
    ProductResponse create(ProductDTO productDTO) throws DataNotFoundException;
    Page<ProductResponse> searchProducts(int cateId, String keyword, PageRequest pageRequest);
    List<Product> getAll();
    ProductResponse update(ProductDTO productDTO, int id) throws DataNotFoundException;
    Product getById(int id) throws DataNotFoundException;
    ProductResponse getProductDetailById(int id) throws DataNotFoundException;
    List<ProductResponse> getProductDetailsByIds(List<Integer> productIds);
    void delete(int id) throws DataNotFoundException;
    ProductImage createProductImage(int productId, ProductImageDTO productImageDTO) throws DataNotFoundException;
}
