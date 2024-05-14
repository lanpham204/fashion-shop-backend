package com.shop.controllers;

import com.shop.dtos.RatingDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.response.RatingResponse;
import com.shop.services.interfaces.IRatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final IRatingService ratingService;
    @PostMapping
    public ResponseEntity<?> createRating(@RequestBody @Valid RatingDTO ratingDTO, BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errosMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage
                ).toList();
                return ResponseEntity.badRequest().body(errosMessages);
            }
            RatingResponse createdRating = ratingService.create(ratingDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRating);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<RatingResponse>> getAllRatings() {
        List<RatingResponse> ratings = ratingService.getAll();
        return ResponseEntity.ok(ratings);
    }
    @GetMapping("product/{product_id}")
    public ResponseEntity<?> getRatingsByProductId(@PathVariable("product_id") int productId) {
        List<RatingResponse> ratings = null;
        try {
            ratings = ratingService.getByProductId(productId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRatingById(@PathVariable int id) {
        try {
            RatingResponse rating = ratingService.getById(id);
            return ResponseEntity.ok(rating);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRating( @Valid @RequestBody RatingDTO ratingDTO, @PathVariable int id
            , BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errosMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage
                ).toList();
                return ResponseEntity.badRequest().body(errosMessages);
            }
            RatingResponse updatedRating = ratingService.update(ratingDTO, id);
            return ResponseEntity.ok(updatedRating);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable int id) {
        try {
            ratingService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
