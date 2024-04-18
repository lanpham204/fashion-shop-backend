package com.shop.services.interfaces;

import com.shop.dtos.SizeDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.Size;

import java.util.List;

public interface ISizeService {
    Size create(SizeDTO sizeDTO);
    List<Size> getAll();
    Size update(SizeDTO sizeDTO,int id) throws DataNotFoundException;
    Size getById(int id) throws DataNotFoundException;
    void delete(int id) throws DataNotFoundException;
}
