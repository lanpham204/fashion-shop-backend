package com.shop.controllers;

import com.shop.dtos.ReplyCommentDTO;
import com.shop.exceptions.DataNotFoundException;
import com.shop.response.ReplyCommentResponse;
import com.shop.services.interfaces.IReplyCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/reply-comments")
@RequiredArgsConstructor
public class ReplyCommentController {

    private final IReplyCommentService replyCommentService;

    @PostMapping
    public ResponseEntity<?> createReplyComment(@Valid @RequestBody ReplyCommentDTO replyCommentDTO
            , BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errosMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage
                ).toList();
                return ResponseEntity.badRequest().body(errosMessages);
            }
            ReplyCommentResponse createdReplyComment = replyCommentService.create(replyCommentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReplyComment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/rating/{ratingId}")
    public ResponseEntity<?> getReplyCommentsByRatingId(@PathVariable int ratingId) {
        List<ReplyCommentResponse> replyComments = null;
        try {
            replyComments = replyCommentService.getByRatingId(ratingId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(replyComments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReplyCommentById(@PathVariable int id) {
        try {
            ReplyCommentResponse replyComment = replyCommentService.getById(id);
            return ResponseEntity.ok(replyComment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReplyComment(@Valid @RequestBody ReplyCommentDTO replyCommentDTO,
                                                              @PathVariable int id, BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errosMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage
                ).toList();
                return ResponseEntity.badRequest().body(errosMessages);
            }
            ReplyCommentResponse updatedReplyComment = replyCommentService.update(replyCommentDTO, id);
            return ResponseEntity.ok(updatedReplyComment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReplyComment(@PathVariable int id) {
        try {
            replyCommentService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
