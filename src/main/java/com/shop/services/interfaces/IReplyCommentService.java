package com.shop.services.interfaces;

import com.shop.dtos.ReplyCommentDTO;
import com.shop.dtos.ReplyCommentDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.response.ReplyCommentResponse;

import java.util.List;

public interface IReplyCommentService {
    ReplyCommentResponse create(ReplyCommentDTO replyCommentDTO) throws DataNotFoundException;
    List<ReplyCommentResponse> getByRatingId(int ratingId) throws DataNotFoundException;
    ReplyCommentResponse update(ReplyCommentDTO replyCommentDTO,int id) throws DataNotFoundException;
    ReplyCommentResponse getById(int id) throws DataNotFoundException;
    void delete(int id) throws DataNotFoundException;
}
