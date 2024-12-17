package org.lesson08.service;

import org.lesson08.repository.exception.EntityNotFound;
import org.lesson08.entity.Article;
import org.lesson08.repository.article.ArticleRepositoryInterface;
import org.lesson08.service.exception.ArticleDeleteException;
import org.lesson08.service.exception.ArticleFindException;
import org.lesson08.service.exception.ArticleUpdateException;

import java.util.List;
import java.util.Set;

public class ArticleService {
  private final ArticleRepositoryInterface articles;
  public ArticleService(ArticleRepositoryInterface articles) {
    this.articles = articles;
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
    Article article = new Article(null, name, tags);
    return articles.create(article);
  }

  public void delete(long id) throws ArticleDeleteException {
    try {
      articles.delete(id);
    } catch (EntityNotFound e) {
      throw new ArticleDeleteException(e.getMessage(), e);
    }
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

  public void createMultiple(List<Article> articleList) {
    articles.createMultiple(articleList);
  }
}
