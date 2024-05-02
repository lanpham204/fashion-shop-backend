package com.shop.services;

import com.shop.dtos.CategoryDTO;
import com.shop.dtos.OrderDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.Category;
import com.shop.models.Order;
import com.shop.repositories.CategoryRepository;
import com.shop.response.CategoryResponse;
import com.shop.services.interfaces.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public CategoryResponse create(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        categoryRepository.save(category);
        return modelMapper.map(category,CategoryResponse.class);
    }

    @Override
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll().stream().map(category -> modelMapper.map(category,CategoryResponse.class)).toList();
    }

    @Override
    public CategoryResponse update(CategoryDTO categoryDTO, int id) throws DataNotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find category with id: " + id));
        modelMapper.typeMap(CategoryDTO.class, Category.class)
                .addMappings(modelMapper -> modelMapper.skip(Category::setId));
        modelMapper.map(categoryDTO, category);
        categoryRepository.save(category);
        return modelMapper.map(category,CategoryResponse.class);
    }

    @Override
    public CategoryResponse getById(int id) throws DataNotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find category with id: " + id));
        return modelMapper.map(category,CategoryResponse.class);
    }

    @Override
    public void delete(int id) throws DataNotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find category with id: " + id));
        categoryRepository.delete(category);
    }
}
