package org.lesson06.controller.response;

import org.lesson06.entity.Comment;

public class CommentResponse {
  private final long id;
  private final String text;

  public CommentResponse(Comment comment) {
    this.id = comment.getId().getValue();
    this.text = comment.getText();
  }

  public long getId() {
    return id;
  }

  public String getText() {
    return text;
  }
}
