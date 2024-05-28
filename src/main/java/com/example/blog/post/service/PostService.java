package com.example.blog.post.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.blog.common.code.BlogErrorCode;
import com.example.blog.common.exception.BlogException;
import com.example.blog.common.jwt.JwtUtil;
import com.example.blog.post.dto.PostRequestDto;
import com.example.blog.post.dto.PostResponseDto;
import com.example.blog.post.entity.PostEntity;
import com.example.blog.post.repository.PostRepository;
import com.example.blog.user.entity.UserEntity;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * PostService.
 */
@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final JwtUtil jwtUtil;

  /**
   * Create Post.
   */
  @Transactional
  public PostResponseDto createPost(PostRequestDto requestDto, HttpServletRequest request) {

    // 토큰 체크 추가
    UserEntity userEntity = jwtUtil.checkToken(request);

    if (userEntity == null) {
      throw new BlogException(BlogErrorCode.NOT_FOUND_USER, null);
    }

    PostEntity postEntity = PostEntity.builder()
        .requestDto(requestDto)
        .userEntity(userEntity)
        .build();

    postRepository.save(postEntity);
    return new PostResponseDto(postEntity);
  }

  /**
   * Get all post.
   */
  @Transactional(readOnly = true) // readOnly true인 경우, JPA 영속성 컨텍스트에 갱신되지 않기 때문에, 조회 시 false로 설정하는 것보다 더 빠르게 조회가 가능함.
  public List<PostResponseDto> getPostList() {
    List<PostEntity> postEntities = postRepository.findAllByOrderByModifiedAtDesc();

    List<PostResponseDto> postResponseDto = new ArrayList<>();

    postEntities.forEach(postEntity -> postResponseDto.add(new PostResponseDto(postEntity)));

    return postResponseDto;
  }

  /**
   * Get post by id.
   */
  @Transactional(readOnly = true)
  public PostResponseDto getPost(Long id) {
    PostEntity postEntity = postRepository.findById(id)
        .orElseThrow(() -> new BlogException(BlogErrorCode.NOT_FOUND_POST, null));
    return new PostResponseDto(postEntity);
  }

  /**
   * Update post by id.
   */
  @Transactional
  public PostResponseDto updatePost(Long id, PostRequestDto requestDto, HttpServletRequest request) {

    // 토큰 체크 추가
    UserEntity userEntity = jwtUtil.checkToken(request);

    if (userEntity == null) {
      throw new BlogException(BlogErrorCode.NOT_FOUND_USER, null);
    }

    PostEntity postEntity = postRepository.findById(id).orElseThrow(
        () -> new BlogException(BlogErrorCode.NOT_FOUND_POST, null)
    );

    if (!postEntity.getUserEntity().equals(userEntity)) {
      throw new BlogException(BlogErrorCode.UNAUTHORIZED_USER, null);
    }

    postEntity.update(requestDto);
    return new PostResponseDto(postEntity);
  }

  /**
   * Delete post.
   */
  @Transactional
  public void deletePost(Long id, HttpServletRequest request) {

    // 토큰 체크 추가
    UserEntity userEntity = jwtUtil.checkToken(request);

    if (userEntity == null) {
      throw new BlogException(BlogErrorCode.NOT_FOUND_USER, null);
    }

    PostEntity postEntity = postRepository.findById(id).orElseThrow(
        () -> new BlogException(BlogErrorCode.NOT_FOUND_POST, null)
    );

    if (postEntity.getUserEntity().equals(userEntity)) {
      postRepository.delete(postEntity);
    }
  }
}