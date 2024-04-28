package com.shop.services.interfaces;

import com.shop.dtos.ColorProductDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.ColorProduct;
import com.shop.models.ColorProductId;

import java.util.List;

public interface IColorProductService {
    ColorProduct create(ColorProductDTO colorProductDTO) throws DataNotFoundException;
    List<ColorProduct> getByProductId(int productId) throws DataNotFoundException;
//    ColorProduct update(ColorProductDTO colorProductDTO,int id) throws DataNotFoundException;
//    ColorProduct getById(int id) throws DataNotFoundException;
    void delete(ColorProductId colorProductId) throws DataNotFoundException;
}
