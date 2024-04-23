package com.shop.controllers;

import com.shop.dtos.ColorDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.Color;
import com.shop.services.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/colors")
@RequiredArgsConstructor
public class ColorController {

    private final ColorService colorService;
    @PostMapping
    public ResponseEntity<Color> createColor(@RequestBody ColorDTO colorDTO) {
        Color createdColor = colorService.create(colorDTO);
        return new ResponseEntity<>(createdColor, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Color>> getAllColors() {
        List<Color> colors = colorService.getAll();
        return new ResponseEntity<>(colors, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getColorById(@PathVariable("id") int id) {
        try {
            Color color = colorService.getById(id);
            return new ResponseEntity<>(color, HttpStatus.OK);
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateColor(@PathVariable("id") int id, @RequestBody ColorDTO colorDTO) {
        try {
            Color updatedColor = colorService.update(colorDTO, id);
            return new ResponseEntity<>(updatedColor, HttpStatus.OK);
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteColor(@PathVariable("id") int id) {
        try {
            colorService.delete(id);
            return ResponseEntity.noContent().build();

        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

