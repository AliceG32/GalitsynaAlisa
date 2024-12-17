package org.lesson08.service.exception;

public class ArticleDeleteException extends Throwable {
  public ArticleDeleteException(String errorMessage, Throwable cause) {
    super(errorMessage, cause);
  }
}
