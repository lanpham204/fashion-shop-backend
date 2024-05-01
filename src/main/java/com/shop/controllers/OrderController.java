package com.shop.controllers;

import com.shop.configuration.VnpayConfig;
import com.shop.dtos.OrderDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.OrderStatus;
import com.shop.response.OrderListResponse;
import com.shop.response.OrderResponse;
import com.shop.response.OrderVNPAYResponse;
import com.shop.services.interfaces.IOrderService;
import io.swagger.v3.core.util.Json;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.shop.configuration.VnpayConfig.vnp_Command;
import static com.shop.configuration.VnpayConfig.vnp_Version;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;
    private final ModelMapper modelMapper;
    @PostMapping
    public ResponseEntity<?> createOrder( @Valid @RequestBody OrderDTO orderDTO, BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errosMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage
                ).toList();
                return ResponseEntity.badRequest().body(errosMessages);
            }
            OrderResponse orderResponse = orderService.create(orderDTO);
            return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/create-payment")
    public ResponseEntity<?> createPayment(@RequestParam BigDecimal amount,
                                           HttpServletRequest request,
                                           @RequestParam int orderId)  {
        try {
            String payment = orderService.createPayment(amount,orderId, request);
            return ResponseEntity.ok(OrderVNPAYResponse.builder()
                    .url(payment)
                    .build());
        }  catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/payment-callback")
    public ResponseEntity<?> transaction(
            @RequestParam(value = "vnp_ResponseCode") String responseCode,
            @RequestParam(value = "orderId") String orderId,
            HttpServletResponse response
    ) throws IOException {
        try {
            if (responseCode.equals("00")) {
                OrderResponse order = orderService.getById(Integer.parseInt(orderId));
                order.setStatus(OrderStatus.PENDING);
                orderService.update(modelMapper.map(order,OrderDTO.class),order.getId());
                response.sendRedirect("http://localhost:4200/my-orders");
                return ResponseEntity.ok("success");
            } else {
                response.sendRedirect("http://localhost:4200/my-orders");
                return ResponseEntity.badRequest().body("failed");
            }
        }  catch (Exception e) {
            return ResponseEntity.badRequest().body("failed");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders(@RequestParam(value = "keyword", required = false,defaultValue = "") String keyword,
                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                          @RequestParam(value = "size", defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<OrderResponse> orderResponsePage = orderService.searchOrders(keyword, pageRequest);
        List<OrderResponse> orderResponses = orderResponsePage.getContent();
        int totalPages = orderResponsePage.getTotalPages();
        return new ResponseEntity<>(OrderListResponse.builder()
                .orders(orderResponses)
                .totalPages(totalPages)
                .build(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable int id) {
        try {
            OrderResponse orderResponse = orderService.getById(id);
            return new ResponseEntity<>(orderResponse, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("user/{user_id}")
    public ResponseEntity<?> getOrderByUserId(@PathVariable("user_id") int userId) {
        try {
            List<OrderResponse> orderResponses = orderService.getAllByUserId(userId);
            return new ResponseEntity<>(orderResponses, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@Valid  @RequestBody OrderDTO orderDTO, @PathVariable int id, BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errosMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage
                ).toList();
                return ResponseEntity.badRequest().body(errosMessages);
            }
            OrderResponse orderResponse = orderService.update(orderDTO, id);
            return new ResponseEntity<>(orderResponse, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable int id) {
        try {
            orderService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
