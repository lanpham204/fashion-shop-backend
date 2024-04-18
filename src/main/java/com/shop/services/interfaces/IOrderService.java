package com.shop.services.interfaces;

import com.shop.dtos.OrderDTO;
import com.shop.dtos.ProductImageDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.Order;
import com.shop.models.Product;
import com.shop.response.ProductImageResponse;
import com.shop.response.OrderResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;

public interface IOrderService {
    OrderResponse create(OrderDTO orderDTO) throws DataNotFoundException;
    Page<OrderResponse> getAll(String keyword, Pageable pageable);
    OrderResponse update(OrderDTO orderDTO,int id) throws DataNotFoundException;
    OrderResponse getById(int id) throws DataNotFoundException;
    void delete(int id) throws DataNotFoundException;
    List<OrderResponse> getAllByUserId(int userId) throws DataNotFoundException;
     String createPayment(BigDecimal amount,int orderId, HttpServletRequest request) throws UnsupportedEncodingException;
}
