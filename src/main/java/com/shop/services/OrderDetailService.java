package com.shop.services;

import com.shop.dtos.OrderDTO;
import com.shop.dtos.OrderDetailDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.Order;
import com.shop.models.OrderDetail;
import com.shop.models.Product;
import com.shop.repositories.OrderDetailRepository;
import com.shop.repositories.OrderRepository;
import com.shop.repositories.ProductRepository;
import com.shop.response.OrderDetailResponse;
import com.shop.services.interfaces.IOrderDetaiService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetaiService {
    private final ModelMapper modelMapper;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    public OrderDetailResponse create(OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("(Cannot found product with id: " + orderDetailDTO.getProductId()));
        Order order = orderRepository.findById(orderDetailDTO.getOrderId()).orElseThrow(
                () -> new DataNotFoundException("Cannot found order with id: "+orderDetailDTO.getOrderId()));
        modelMapper.typeMap(OrderDetailDTO.class, OrderDetail.class)
                .addMappings(mapper -> mapper.skip(OrderDetail::setId));
        OrderDetail orderDetail = modelMapper.map(orderDetailDTO, OrderDetail.class);
        orderDetailRepository.save(orderDetail);
        return modelMapper.map(orderDetail, OrderDetailResponse.class);
    }

    @Override
    public List<OrderDetailResponse> getAll() {
        return orderDetailRepository.findAll().stream()
                .map(orderDetail -> modelMapper.map(orderDetail, OrderDetailResponse.class)).toList();
    }

    @Override
    public OrderDetailResponse update(OrderDetailDTO orderDetailDTO, int id) throws DataNotFoundException {
        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("(Cannot found product with id: " + orderDetailDTO.getProductId()));
        Order order = orderRepository.findById(orderDetailDTO.getOrderId()).orElseThrow(
                () -> new DataNotFoundException("Cannot found order with id: "+orderDetailDTO.getOrderId()));
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("(Cannot found order detail with id: " + id));
        modelMapper.typeMap(OrderDetailDTO.class, OrderDetail.class)
                .addMappings(mapper -> mapper.skip(OrderDetail::setId));
        modelMapper.map(orderDetailDTO,orderDetail);
        return  modelMapper.map(orderDetail, OrderDetailResponse.class);
    }

    @Override
    public OrderDetailResponse getById(int id) throws DataNotFoundException {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("(Cannot found order detail with id: " + id));
        return modelMapper.map(orderDetail, OrderDetailResponse.class);
    }

    @Override
    public void delete(int id) throws DataNotFoundException {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("(Cannot found order detail with id: " + id));
        orderDetailRepository.delete(orderDetail);
    }
}
