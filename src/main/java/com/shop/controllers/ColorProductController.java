package com.shop.controllers;


import com.shop.dtos.ColorProductDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.ColorProduct;
import com.shop.models.ColorProductId;
import com.shop.services.interfaces.IColorProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/color-products")
@RequiredArgsConstructor
public class ColorProductController {
    private final IColorProductService colorProductService;

    @PostMapping("")
    public ResponseEntity<?> createColorProduct(@RequestBody ColorProductDTO colorProductDTO) {
        try {
            ColorProduct createdColorProduct = colorProductService.create(colorProductDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdColorProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getColorProductsByProductId(@PathVariable int productId) throws DataNotFoundException {
        try {
            List<ColorProduct> colorProducts = colorProductService.getByProductId(productId);
            return ResponseEntity.status(HttpStatus.OK).body(colorProducts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @DeleteMapping()
    public ResponseEntity<String> deleteColorProduct(@RequestParam("product_id") int productId, @RequestParam("color_id") int colorId) {
        try {
            colorProductService.delete(new ColorProductId(productId, colorId));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
        }
    }
}