package org.lesson08.service.exception;

public class ArticleUpdateException extends Throwable {
  public ArticleUpdateException(String errorMessage, Throwable cause) {
    super(errorMessage, cause);
  }
}
