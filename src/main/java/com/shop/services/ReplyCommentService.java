package com.shop.services;

import com.shop.dtos.OrderDTO;
import com.shop.dtos.ReplyCommentDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.models.Order;
import com.shop.models.Rating;
import com.shop.models.ReplyComment;
import com.shop.repositories.RatingRepository;
import com.shop.repositories.ReplyCommentRepository;
import com.shop.response.ReplyCommentResponse;
import com.shop.services.interfaces.IReplyCommentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ReplyCommentService implements IReplyCommentService {
    private final RatingRepository ratingRepository;
    private final ReplyCommentRepository replyCommentRepository;
    private final ModelMapper modelMapper;
    @Override
    public ReplyCommentResponse create(ReplyCommentDTO replyCommentDTO) throws DataNotFoundException {
        Rating rating = ratingRepository.findById(replyCommentDTO.getRatingId())
                .orElseThrow(() -> new DataNotFoundException("Cannot found rating with id: " + replyCommentDTO.getRatingId()));
        modelMapper.typeMap(ReplyCommentDTO.class, ReplyComment.class)
                .addMappings(mapper -> mapper.skip(ReplyComment::setId));
        ReplyComment replyComment = modelMapper.map(replyCommentDTO, ReplyComment.class);
        replyCommentRepository.save(replyComment);
        return modelMapper.map(replyComment,ReplyCommentResponse.class);
    }

    @Override
    public List<ReplyCommentResponse> getByRatingId(int ratingId) throws DataNotFoundException {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new DataNotFoundException("Cannot found rating with id: " + ratingId));
        return replyCommentRepository.findByRatingId(ratingId).stream().map(replyComment -> modelMapper.map(replyComment,ReplyCommentResponse.class)).toList();
    }

    @Override
    public ReplyCommentResponse update(ReplyCommentDTO replyCommentDTO, int id) throws DataNotFoundException {
        Rating rating = ratingRepository.findById(replyCommentDTO.getRatingId())
                .orElseThrow(() -> new DataNotFoundException("Cannot found rating with id: " + replyCommentDTO.getRatingId()));
        ReplyComment replyComment = replyCommentRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot found reply comment with id: " + id));
        modelMapper.map(replyCommentDTO,replyComment);
        replyCommentRepository.save(replyComment);
        return modelMapper.map(replyComment,ReplyCommentResponse.class);
    }

    @Override
    public ReplyCommentResponse getById(int id) throws DataNotFoundException {
        ReplyComment replyComment = replyCommentRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot found reply comment with id: " + id));
        return modelMapper.map(replyComment,ReplyCommentResponse.class);
    }

    @Override
    public void delete(int id) throws DataNotFoundException {
        ReplyComment replyComment = replyCommentRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot found reply comment with id: " + id));
        replyCommentRepository.delete(replyComment);
    }
}
