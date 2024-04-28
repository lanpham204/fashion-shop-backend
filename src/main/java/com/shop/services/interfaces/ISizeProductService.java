package com.shop.services.interfaces;

import com.shop.dtos.SizeProductDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.SizeProduct;
import com.shop.models.SizeProductId;
import com.shop.models.SizeProduct;

import java.util.List;

public interface ISizeProductService {
    SizeProduct create(SizeProductDTO sizeProductDTO) throws DataNotFoundException;
    List<SizeProduct> getByProductId(int productId) throws DataNotFoundException;
    //    SizeProduct update(SizeProductDTO SizeProductDTO,int id) throws DataNotFoundException;
//    SizeProduct getById(int id) throws DataNotFoundException;
    void delete(SizeProductId sizeProductId) throws DataNotFoundException;

}
