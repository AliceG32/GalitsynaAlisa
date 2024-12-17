package org.lesson08.service.exception;

public class ArticleFindException extends Throwable {
  public ArticleFindException(String errorMessage, Throwable cause) {
    super(errorMessage, cause);
  }
}
