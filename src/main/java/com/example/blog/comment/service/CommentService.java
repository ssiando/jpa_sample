package com.example.blog.comment.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.blog.comment.dto.CommentRequestDto;
import com.example.blog.comment.dto.CommentResponseDto;
import com.example.blog.comment.entity.CommentEntity;
import com.example.blog.comment.repository.CommentRepository;
import com.example.blog.common.code.BlogErrorCode;
import com.example.blog.common.constant.ProjConst;
import com.example.blog.common.dto.ApiResult;
import com.example.blog.common.exception.BlogException;
import com.example.blog.common.jwt.JwtUtil;
import com.example.blog.post.entity.PostEntity;
import com.example.blog.post.repository.PostRepository;
import com.example.blog.user.entity.UserEntity;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * CommentService.
 */
@Service
@RequiredArgsConstructor
public class CommentService {

  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final JwtUtil jwtUtil;

  /**
   * Create Comment.
   */
  @Transactional
  public CommentResponseDto createComment(CommentRequestDto commentRequestDto, HttpServletRequest request) {

    /*
     * 토큰 검증.
     */
    UserEntity userEntity = jwtUtil.checkToken(request);

    if (userEntity == null) {
      throw new BlogException(BlogErrorCode.NOT_FOUND_USER, null);
    }

    /*
     * 댓글을 작성할 게시글이 존재하는지 확인.
     */
    PostEntity postEntity = postRepository.findById(commentRequestDto.getPostId()).orElseThrow(
        () -> new BlogException(BlogErrorCode.NOT_FOUND_POST, null)
    );

    /*
     * 댓글 저장.
     */
    CommentEntity entity = new CommentEntity();
    entity.setContent(commentRequestDto.getContent());
    entity.setUserEntity(userEntity);
    entity.setPostEntity(postEntity);

    commentRepository.save(entity);

    return CommentResponseDto.builder()
        .postId(postEntity.getPostId())
        .commentId(entity.getCommentId())
        .content(entity.getContent())
        .username(userEntity.getUsername())
        .build();
  }

  /**
   * Update comment.
   */
  @Transactional
  public CommentResponseDto updateComment(CommentRequestDto commentRequestDto, Long commentId, HttpServletRequest request) {

    /*
     * 토큰 검증.
     */
    UserEntity userEntity = this.checkJwtToken(request);

    /*
     * 작성한 댓글이 존재하는지 확인.
     */
    CommentEntity commentEntity = this.checkValidComment(commentId);

    /*
     * 수정하려고 하는 댓글의 작성자가 본인인지, 관리자 계정으로 수정하려고 하는지 확인.
     */
    if (this.checkValidUser(userEntity, commentEntity)) {
      throw new BlogException(BlogErrorCode.UNAUTHORIZED_USER, null);
    }

    commentEntity.setContent(commentRequestDto.getContent());
    commentRepository.save(commentEntity);

    return CommentResponseDto.builder()
        .postId(commentEntity.getPostEntity().getPostId())
        .commentId(commentEntity.getCommentId())
        .username(commentEntity.getUserEntity().getUsername())
        .content(commentEntity.getContent())
        .build();
  }

  /**
   * Delete comment.
   */
  @Transactional
  public ApiResult deleteComment(Long commentId, HttpServletRequest request) {

    /*
     * 토큰 검증.
     */
    UserEntity userEntity = this.checkJwtToken(request);

    /*
     * 삭제하려고 하는 댓글이 존재하는지 확인.
     */
    CommentEntity commentEntity = this.checkValidComment(commentId);

    /*
     * 삭제하려고 하는 댓글의 작성자가 본인인지, 관리자 계정으로 수정하려고 하는지 확인.
     */
    if (this.checkValidUser(userEntity, commentEntity)) {
      throw new BlogException(BlogErrorCode.UNAUTHORIZED_USER, null);
    }

    commentRepository.delete(commentEntity);

    return ApiResult.builder()
        .msg(ProjConst.API_CALL_SUCCESS)
        .statusCode(HttpStatus.OK.value())
        .build();
  }

  /**
   * Check JWT Token section.
   */
  private UserEntity checkJwtToken(HttpServletRequest request) {
    UserEntity userEntity = jwtUtil.checkToken(request);

    if (userEntity == null) {
      throw new BlogException(BlogErrorCode.NOT_FOUND_USER, null);
    }

    return userEntity;
  }

  /**
   * Check valid comment.
   */
  private CommentEntity checkValidComment(Long commentId) {
    return commentRepository.findById(commentId).orElseThrow(
        () -> new BlogException(BlogErrorCode.NOT_FOUND_COMMENT, null)
    );
  }

  /**
   * Check valid user.
   */
  private boolean checkValidUser(UserEntity userEntity, CommentEntity commentEntity) {
    return !(userEntity.getUsername().equals(commentEntity.getUserEntity().getUsername())
        && userEntity.getRole().equals(ProjConst.ADMIN_ROLE));
  }
}
