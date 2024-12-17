package org.lesson08.controller.response;

import org.lesson08.entity.Comment;

public class CommentResponse {
  private final long id;
  private final String text;

  public CommentResponse(Comment comment) {
    this.id = comment.getId();
    this.text = comment.getText();
  }

  public long getId() {
    return id;
  }

  public String getText() {
    return text;
  }
}
