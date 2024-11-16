package org.lesson06.service;

import org.lesson06.repository.comment.CommentRepositoryInterface;
import org.lesson06.repository.exception.EntityNotFound;
import org.lesson06.entity.Article;
import org.lesson06.repository.article.ArticleRepositoryInterface;
import org.lesson06.service.exception.ArticleDeleteException;
import org.lesson06.service.exception.ArticleFindException;
import org.lesson06.service.exception.ArticleUpdateException;

import java.util.List;
import java.util.Set;

public class ArticleService {
  private final ArticleRepositoryInterface articles;
  private final CommentRepositoryInterface comments;

  public ArticleService(ArticleRepositoryInterface articles, CommentRepositoryInterface comments) {
    this.articles = articles;
    this.comments = comments;
  }

  public List<Article> findAll() {
    return articles.findAll();
  }

  public Article findById(long id) throws ArticleFindException {
    try {
      return articles.getById(id);
    } catch (EntityNotFound e) {
      throw new ArticleFindException(e.getMessage(), e);
    }
  }

  public long create(String name, Set<String> tags) {
    Article article = new Article(name, tags);
    return articles.create(article);
  }

  public void delete(long id) throws ArticleDeleteException {
    try {
      articles.getById(id);
    } catch (EntityNotFound e) {
      throw new ArticleDeleteException(e.getMessage(), e);
    }
  }

  public void deleteCommentsByArticleId(long id) throws ArticleDeleteException {
      comments.deleteByArticleId(id);
  }

  public void update(long articleId, String name, Set<String> tags) throws ArticleUpdateException {
    Article article;
    try {
      article = articles.getById(articleId);
    } catch (EntityNotFound e) {
      throw new ArticleUpdateException(e.getMessage(), e);
    }

    try {
      articles.update(article.withName(name).withTags(tags));
    } catch (EntityNotFound e) {
      throw new ArticleUpdateException(e.getMessage(), e);
    }
  }
}
