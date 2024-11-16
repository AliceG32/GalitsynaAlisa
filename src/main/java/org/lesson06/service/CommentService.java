package org.lesson06.service;

import java.util.List;
import java.util.Set;
import org.lesson06.entity.Article;
import org.lesson06.entity.ArticleId;
import org.lesson06.entity.Comment;
import org.lesson06.repository.article.ArticleRepositoryInterface;
import org.lesson06.repository.comment.CommentRepositoryInterface;
import org.lesson06.repository.exception.EntityNotFound;
import org.lesson06.service.exception.*;

public class CommentService {
  private final ArticleRepositoryInterface articles;
  private final CommentRepositoryInterface comments;

  public CommentService(ArticleRepositoryInterface articles, CommentRepositoryInterface comments) {
    this.articles = articles;
    this.comments = comments;
  }

  public long create(long articleId, String name) throws CommentCreateException {
    try {
      articles.getById(articleId);
    } catch (EntityNotFound e) {
      throw new CommentCreateException(e.getMessage(), e);
    }
    Comment comment = new Comment(new ArticleId(articleId), name);
    return comments.create(comment);
  }

  public void delete(long id) throws CommentDeleteException {
    try {
      comments.delete(id);
    } catch (EntityNotFound e) {
      throw new CommentDeleteException(e.getMessage(), e);
    }
  }

  public List<Comment> findAllByArticleId(long articleId) {
    return comments.findAllByArticleId(articleId);
  }

  public Long getCountByArticleId(long articleId) {
    return comments.getCountByArticleId(articleId);
  }
}
