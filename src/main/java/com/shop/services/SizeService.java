package com.shop.services;

import com.shop.dtos.SizeDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.Size;
import com.shop.repositories.SizeRepository;
import com.shop.services.interfaces.ISizeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SizeService implements ISizeService {
    private final SizeRepository sizeRepository;
    private final ModelMapper modelMapper;


    @Override
    public Size create(SizeDTO sizeDTO) {
        if(sizeDTO.getSize() != null) {
            sizeDTO.setSize(sizeDTO.getSize().toUpperCase());
            Size size = modelMapper.map(sizeDTO, Size.class);
            sizeRepository.save(size);
            return size;
        }
        return null;
    }

    @Override
    public List<Size> getAll() {
        return sizeRepository.findAll();
    }

    @Override
    public Size update(SizeDTO sizeDTO, int id) throws DataNotFoundException {
        Size size = sizeRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Cannot find size with id: "+id));
        sizeDTO.setSize(sizeDTO.getSize().toUpperCase());
        Size updateSize = modelMapper.map(sizeDTO, Size.class);
        updateSize.setId(size.getId());
        sizeRepository.save(updateSize);
        return updateSize;
    }

    @Override
    public Size getById(int id) throws DataNotFoundException {
        return sizeRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Cannot find size with id: "+id));
    }

    @Override
    public void delete(int id) throws DataNotFoundException {
        Size size = sizeRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Cannot find size with id: "+id));
        sizeRepository.delete(size);
    }
}
