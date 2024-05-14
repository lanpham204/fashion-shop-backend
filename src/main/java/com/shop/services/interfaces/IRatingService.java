package com.shop.services.interfaces;

import com.shop.dtos.RatingDTO;
import com.shop.dtos.RatingDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.response.RatingResponse;
import com.shop.response.RatingResponse;

import java.util.List;

public interface IRatingService {
    RatingResponse create(RatingDTO ratingDTO) throws DataNotFoundException;
    List<RatingResponse> getAll();
    List<RatingResponse> getByProductId(int productId) throws DataNotFoundException;
    RatingResponse update(RatingDTO ratingDTO,int id) throws DataNotFoundException;
    RatingResponse getById(int id) throws DataNotFoundException;
    void delete(int id) throws DataNotFoundException;
}
