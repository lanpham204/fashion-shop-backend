package com.shop.services.interfaces;

import com.shop.dtos.OrderDetailDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.response.OrderDetailResponse;

import java.util.List;

public interface IOrderDetaiService {
    OrderDetailResponse create(OrderDetailDTO orderDetailDTO) throws DataNotFoundException;
    List<OrderDetailResponse> getAll();
    OrderDetailResponse update(OrderDetailDTO orderDetailDTO,int id) throws DataNotFoundException;
    OrderDetailResponse getById(int id) throws DataNotFoundException;
    void delete(int id) throws DataNotFoundException;
}
