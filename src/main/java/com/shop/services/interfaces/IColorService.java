package com.shop.services.interfaces;

import com.shop.dtos.ColorDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.Color;

import java.util.List;

public interface IColorService {
    Color create(ColorDTO colorDTO);
    List<Color> getAll();
    Color update(ColorDTO colorDTO,int id) throws DataNotFoundException;
    Color getById(int id) throws DataNotFoundException;
    void delete(int id) throws DataNotFoundException;
}
