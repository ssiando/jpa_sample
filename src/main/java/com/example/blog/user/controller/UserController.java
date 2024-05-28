package com.example.blog.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog.common.dto.ApiResult;
import com.example.blog.user.dto.LoginRequestDto;
import com.example.blog.user.dto.SignupRequestDto;
import com.example.blog.user.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


/**
 * UserController.
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class UserController {

  private final UserService userService;

  /**
   * Signup.
   */
  @PostMapping("/signup")
  public ApiResult signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {

    userService.signup(signupRequestDto);

    return new ApiResult("회원가입 성공", HttpStatus.OK.value()); // 회원가입 성공시 ApiResult Dto를 사용하여 성공메세지와 statusCode를 띄움
  }

  /**
   * Login.
   */
  @PostMapping("/login")
  public ApiResult login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {

    userService.login(loginRequestDto, response);

    return new ApiResult("로그인 성공", HttpStatus.OK.value()); // 로그인 성공시 ApiResult Dto를 사용하여 성공메세지와 statusCode를 띄움
  }
}