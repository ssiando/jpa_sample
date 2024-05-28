package com.example.blog.post.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog.common.dto.ApiResult;
import com.example.blog.post.dto.PostRequestDto;
import com.example.blog.post.dto.PostResponseDto;
import com.example.blog.post.service.PostService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * PostController.
 */
@RestController
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  /**
   * Create post.
   */
  @PostMapping("/api/post")
  public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, HttpServletRequest request) {  // 객체 형식으로 넘어오기 때문에 RequestBody를 사용
    return postService.createPost(requestDto, request);
  }

  /**
   * Get post list.
   */
  @GetMapping("/api/post")
  public List<PostResponseDto> getPostList() {
    return postService.getPostList();
  }

  /**
   * Get certain post.
   */
  @GetMapping("/api/post/{id}")
  public PostResponseDto getPost(@PathVariable Long id) {
    return postService.getPost(id);
  }

  /**
   * Update post.
   */
  @PutMapping("/api/post/{id}")
  public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, HttpServletRequest request) {
    return postService.updatePost(id, requestDto, request);
  }

  /**
   * Delete post.
   */
  @DeleteMapping("/api/post/{id}")
  public ApiResult deletePost(@PathVariable Long id, HttpServletRequest request) {
    postService.deletePost(id, request);
    return new ApiResult("게시글 삭제 성공", HttpStatus.OK.value()); // 게시글 삭제 성공시 ApiResult Dto를 사용하여 성공메세지와 statusCode를 띄움
  }
}
