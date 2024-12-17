package org.lesson08.service.exception;

public class CommentDeleteException extends Throwable {
  public CommentDeleteException(String errorMessage, Throwable cause) {
    super(errorMessage, cause);
  }
}
