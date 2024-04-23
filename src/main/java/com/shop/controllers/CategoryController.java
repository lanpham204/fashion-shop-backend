package com.shop.controllers;

import com.shop.dtos.CategoryDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.services.interfaces.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @PostMapping("")
    public ResponseEntity<?> createCategory(@RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.create(categoryDTO));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable int id) throws DataNotFoundException {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateCategory(@PathVariable int id, @RequestBody CategoryDTO categoryDTO) throws DataNotFoundException {
        return ResponseEntity.ok(categoryService.update(categoryDTO, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable int id) throws DataNotFoundException {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
