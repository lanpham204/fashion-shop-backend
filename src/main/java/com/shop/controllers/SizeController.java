package com.shop.controllers;

import com.shop.dtos.SizeDTO;
import com.shop.services.interfaces.ISizeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/sizes")
@RequiredArgsConstructor
public class SizeController {
    private final ISizeService sizeService;
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(sizeService.getAll());
    }
    @PostMapping("")
    public ResponseEntity<?> createSize(@RequestBody @Valid SizeDTO sizeDTO) {
        return ResponseEntity.ok(sizeService.create(sizeDTO));
    }
    @GetMapping("{id}")
    public ResponseEntity<?> getSizeById(@PathVariable int id)  {
        try {
            return ResponseEntity.ok(sizeService.getById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("{id}")
    public ResponseEntity<?> updateSize(@PathVariable int id,@RequestBody @Valid SizeDTO sizeDTO) {
        try {
            return ResponseEntity.ok(sizeService.update(sizeDTO,id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteSize(@PathVariable int id) {
        try {
            sizeService.delete(id);
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
