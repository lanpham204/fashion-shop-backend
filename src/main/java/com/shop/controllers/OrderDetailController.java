package com.shop.controllers;

import com.shop.dtos.OrderDetailDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.response.OrderDetailResponse;
import com.shop.services.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order-details")
@RequiredArgsConstructor
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    @PostMapping
    public ResponseEntity<?> createOrderDetail(@RequestBody @Valid OrderDetailDTO orderDetailDTO, BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errosMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage
                ).toList();
                return ResponseEntity.badRequest().body(errosMessages);
            }
            OrderDetailResponse response = orderDetailService.create(orderDetailDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllOrderDetails() {
        List<OrderDetailResponse> orderDetails = orderDetailService.getAll();
        return ResponseEntity.ok(orderDetails);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetailById(@PathVariable int id) {
        try {
            OrderDetailResponse orderDetail = orderDetailService.getById(id);
            return ResponseEntity.ok(orderDetail);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@RequestBody @Valid OrderDetailDTO orderDetailDTO, @PathVariable int id, BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errosMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage
                ).toList();
                return ResponseEntity.badRequest().body(errosMessages);
            }
            OrderDetailResponse updatedOrderDetail = orderDetailService.update(orderDetailDTO, id);
            return ResponseEntity.ok(updatedOrderDetail);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@PathVariable int id) {
        try {
            orderDetailService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

