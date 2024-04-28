package com.shop.controllers;

import com.shop.dtos.SizeProductDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.ColorProduct;
import com.shop.models.SizeProduct;
import com.shop.models.SizeProduct;
import com.shop.models.SizeProductId;
import com.shop.services.interfaces.ISizeProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/size-products")
@RequiredArgsConstructor
public class SizeProductController {
    private final ISizeProductService sizeProductService;

    @PostMapping("")
    public ResponseEntity<?>  createSizeProduct(@RequestBody SizeProductDTO sizeProductDTO) {
        try {
            SizeProduct createdSizeProduct = sizeProductService.create(sizeProductDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSizeProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/{productId}")
    public ResponseEntity<?> getSizeProductsByProductId(@PathVariable int productId) throws DataNotFoundException {
        try {
            List<SizeProduct> sizeProducts = sizeProductService.getByProductId(productId);
            return ResponseEntity.status(HttpStatus.OK).body(sizeProducts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }

    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteSizeProduct(@RequestParam("product_id") int productId, @RequestParam("size_id") int sizeId) {
        try {
            sizeProductService.delete(new SizeProductId(productId, sizeId));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
