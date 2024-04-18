package com.shop.services.interfaces;

import com.shop.dtos.CategoryDTO;
import com.shop.dtos.ProductImageDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.Category;
import com.shop.models.Product;
import com.shop.response.ProductImageResponse;
import com.shop.response.CategoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICategoryService {
    CategoryResponse create(CategoryDTO categoryDTO);
    List<CategoryResponse> getAll();
    CategoryResponse update(CategoryDTO categoryDTO,int id) throws DataNotFoundException;
    CategoryResponse getById(int id) throws DataNotFoundException;
    void delete(int id) throws DataNotFoundException;
}
