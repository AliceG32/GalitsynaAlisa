package org.lesson08.repository.article;

import org.lesson08.repository.exception.EntityNotFound;
import org.lesson08.entity.Article;
import java.util.List;


public interface ArticleRepositoryInterface {
    long nextId();
    long create(Article article);
    Article getById(long id) throws EntityNotFound;
    List<Article> findAll();
    void delete(long id) throws EntityNotFound;
    void update(Article article) throws EntityNotFound;
    void createMultiple(List<Article> articles);
}
