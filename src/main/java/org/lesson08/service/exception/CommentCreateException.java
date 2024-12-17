package org.lesson08.service.exception;

public class CommentCreateException extends Throwable {
  public CommentCreateException(String errorMessage, Throwable cause) {
    super(errorMessage, cause);
  }
}
