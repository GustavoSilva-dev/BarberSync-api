package com.barbersync.barbersync_api.infra.exception;

public class TokenException extends RuntimeException {
  public TokenException(String message) {
    super(message);
  }
}
