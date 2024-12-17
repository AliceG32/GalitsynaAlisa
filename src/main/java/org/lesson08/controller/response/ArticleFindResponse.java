package org.lesson08.controller.response;

import org.lesson08.entity.Article;

import java.util.List;
import java.util.Set;

public class ArticleFindResponse {
  private long id;
  private String name;
  private Set<String> tags;
  private List<CommentResponse> comments;

  public ArticleFindResponse() {
    super();
  }

  public ArticleFindResponse(Article article, List<CommentResponse> comments) {
    this.id = article.getId();
    this.name = article.getName();
    this.tags = article.getTags();
    this.comments = comments;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Set<String> getTags() {
    return tags;
  }

  public List<CommentResponse> getComments() {
    return comments;
  }
}
