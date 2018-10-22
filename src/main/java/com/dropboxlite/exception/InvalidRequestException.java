package com.dropboxlite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidRequestException extends RuntimeException {
  private String reason;

  public InvalidRequestException(String reason) {
    this.reason = reason;
  }

  public String reason() {
    return this.reason;
  }
}
