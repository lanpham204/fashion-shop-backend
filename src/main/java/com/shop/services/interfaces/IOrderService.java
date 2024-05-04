package com.shop.services.interfaces;

import com.shop.dtos.OrderDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.OrderStatus;
import com.shop.response.OrderResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface IOrderService {
    OrderResponse create(OrderDTO orderDTO) throws DataNotFoundException;
    Page<OrderResponse> searchOrders(String keyword, OrderStatus status, PageRequest pageRequest);
    OrderResponse update(OrderDTO orderDTO,int id) throws DataNotFoundException;
    OrderResponse getById(int id) throws DataNotFoundException;
    void delete(int id) throws DataNotFoundException;
    List<OrderResponse> getAllByUserId(int userId) throws DataNotFoundException;
    String createPayment(BigDecimal amount,int orderId, HttpServletRequest request) throws UnsupportedEncodingException;
    long getCountOrderByDate(Date date);
    List<Object> getRevenueByDate(Date date);
    List<Object> getRevenueByMonth(String month, String year);
    List<Object> getRevenueByYear(String year);
    List<Object> getMonthOfYear();

}
