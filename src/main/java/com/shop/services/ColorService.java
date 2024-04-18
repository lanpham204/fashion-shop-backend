package com.shop.services;

import com.shop.dtos.ColorDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.Color;
import com.shop.repositories.ColorRepository;
import com.shop.services.interfaces.IColorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorService implements IColorService {
    private final ColorRepository colorRepository;
    private final ModelMapper modelMapper;

    @Override
    public Color create(ColorDTO colorDTO) {
        Color color = modelMapper.map(colorDTO, Color.class);
        color.setColor(capitalizeFirstLetter(color.getColor()));
        colorRepository.save(color);
        return color;
    }

    @Override
    public List<Color> getAll() {
        return colorRepository.findAll();
    }

    @Override
    public Color update(ColorDTO colorDTO, int id) throws DataNotFoundException {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find color with id: " + id));
        Color colorUpdate = modelMapper.map(colorDTO, Color.class);
        colorUpdate.setId(color.getId());
        colorRepository.save(colorUpdate);
        return colorUpdate;
    }

    @Override
    public Color getById(int id) throws DataNotFoundException {
        return colorRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find color with id: " + id));
    }

    @Override
    public void delete(int id) throws DataNotFoundException {
        Color color = getById(id);
        colorRepository.delete(color);
    }
    public  String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}