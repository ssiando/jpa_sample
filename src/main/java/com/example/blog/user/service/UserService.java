package com.example.blog.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.blog.common.code.BlogErrorCode;
import com.example.blog.common.exception.BlogException;
import com.example.blog.common.jwt.JwtUtil;
import com.example.blog.user.dto.LoginRequestDto;
import com.example.blog.user.dto.SignupRequestDto;
import com.example.blog.user.entity.UserEntity;
import com.example.blog.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * UserService.
 */
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;

  /**
   * Signup.
   */
  @Transactional
  public void signup(SignupRequestDto signupRequestDto) {
    String username = signupRequestDto.getUsername();
    String password = signupRequestDto.getPassword();
    String role = signupRequestDto.getRole();

    // 회원 중복 확인
    Optional<UserEntity> found = userRepository.findByUsername(username);
    if (found.isPresent()) {
      throw new BlogException(BlogErrorCode.IN_USED_USERNAME, null);
    }

    UserEntity userEntity = new UserEntity(username, password, role);
    userRepository.save(userEntity);
  }

  /**
   * Login.
   */
  @Transactional(readOnly = true)
  public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
    String username = loginRequestDto.getUsername();
    String password = loginRequestDto.getPassword();

    // 사용자 확인
    UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(
        () -> new BlogException(BlogErrorCode.NOT_FOUND_USER, null)
    );

    // 비밀번호 확인
    if (!userEntity.getPassword().equals(password)) {
      throw new BlogException(BlogErrorCode.WRONG_PASSWORD, null);
    }
    // JWT Token 생성 및 반환
    response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(userEntity.getUsername()));
  }
}