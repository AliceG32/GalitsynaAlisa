package org.lesson06.repository.article;

import org.lesson06.repository.exception.EntityNotFound;
import org.lesson06.entity.Article;

import java.util.List;

public interface ArticleRepositoryInterface {
    long create(Article article);
    Article getById(long id) throws EntityNotFound;
    List<Article> findAll();
    void delete(long id) throws EntityNotFound;
    void update(Article article) throws EntityNotFound;
}
