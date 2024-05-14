package com.shop.services;

import com.shop.dtos.OrderDTO;
import com.shop.dtos.RatingDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.Order;
import com.shop.models.Product;
import com.shop.models.Rating;
import com.shop.models.User;
import com.shop.repositories.ProductRepository;
import com.shop.repositories.RatingRepository;
import com.shop.repositories.UserRepository;
import com.shop.response.RatingResponse;
import com.shop.services.interfaces.IRatingService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class RatingService implements IRatingService {
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    @Override
    public RatingResponse create(RatingDTO ratingDTO) throws DataNotFoundException {
        User user = userRepository.findById(ratingDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot found user with id: " + ratingDTO.getUserId()));
        Product product = productRepository.findById(ratingDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot found product with id :" + ratingDTO.getProductId()));
        Rating rating = Rating.builder()
                .user(user)
                .value(ratingDTO.getValue())
                .comment(ratingDTO.getComment())
                .product(product)
                .build();
        ratingRepository.save(rating);
        return modelMapper.map(rating,RatingResponse.class);
    }

    @Override
    public List<RatingResponse> getAll() {
        return ratingRepository.findAll().stream().map(rating -> modelMapper.map(rating,RatingResponse.class)).toList();
    }

    @Override
    public List<RatingResponse> getByProductId(int productId) throws DataNotFoundException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Cannot found product with id :" + productId));
        return ratingRepository.findByProductId(productId).stream().map(rating -> modelMapper.map(rating,RatingResponse.class)).toList();
    }

    @Override
    public RatingResponse update(RatingDTO ratingDTO, int id) throws DataNotFoundException {
        User user = userRepository.findById(ratingDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot found user with id: " + ratingDTO.getUserId()));
        Product product = productRepository.findById(ratingDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot found product with id :" + ratingDTO.getProductId()));
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot found rating with id: " + id));
        rating.setComment(ratingDTO.getComment());
        rating.setValue(ratingDTO.getValue());
        ratingRepository.save(rating);
        return modelMapper.map(rating,RatingResponse.class);
    }

    @Override
    public RatingResponse getById(int id) throws DataNotFoundException {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot found rating with id: " + id));
        return modelMapper.map(rating,RatingResponse.class);
    }

    @Override
    public void delete(int id) throws DataNotFoundException {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot found rating with id: " + id));
        ratingRepository.delete(rating);
    }
}
