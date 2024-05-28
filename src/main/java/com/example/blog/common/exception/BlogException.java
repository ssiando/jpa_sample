package com.example.blog.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.blog.common.code.BlogErrorCode;

/**
 * HanghaeBlogException.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST) // response로 들어가는 에러 코드를 400으로 통일
public class BlogException extends RuntimeException {

  private final BlogErrorCode errorCode;

  public BlogException(BlogErrorCode errorCode, Throwable cause) {
    super(errorCode.getErrorMessage(), cause, false, false);
    this.errorCode = errorCode;
  }

  public BlogErrorCode getErrorCode() {
    return this.errorCode;
  }
}
