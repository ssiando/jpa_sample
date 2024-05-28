package com.example.blog.comment.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog.comment.dto.CommentRequestDto;
import com.example.blog.comment.dto.CommentResponseDto;
import com.example.blog.comment.service.CommentService;
import com.example.blog.common.dto.ApiResult;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * CommentController.
 */
@RestController
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  /**
   * Create comment.
   */
  @PostMapping("/api/comment")
  public CommentResponseDto createComment(@RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) {
    return commentService.createComment(commentRequestDto, request);
  }

  /**
   * Update comment.
   */
  @PutMapping("/api/comment/{id}")
  public CommentResponseDto updateComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) {
    return commentService.updateComment(commentRequestDto, id, request);
  }

  /**
   * Delete comment.
   */
  @DeleteMapping("/api/comment/{id}")
  public ApiResult deleteComment(@PathVariable Long id, HttpServletRequest request) {
    return commentService.deleteComment(id, request);
  }
}
