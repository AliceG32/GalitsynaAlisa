package org.lesson08.repository.article;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.lesson08.entity.Article;
import org.lesson08.repository.exception.EntityNotFound;
import java.util.*;

public class PostgresArticleRepository implements ArticleRepositoryInterface {
    private final Handle handle;
    private final Jdbi jdbi;

    public PostgresArticleRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
        this.handle = jdbi.open();
    }

    @Override
    public long nextId() {
        return (long) this.handle.createQuery("SELECT nextval('article_id_seq') AS value")
                        .mapToMap()
                        .first()
                        .get("value");
    }

    @Override
    public long create(Article article) {
        long id = nextId();
        this.handle.createUpdate("INSERT INTO article (id, name, tags) VALUES (:id, :name, :tags)")
                    .bind("id", id)
                    .bind("name", article.getName())
                    .bind("tags", String.join(",", article.getTags()))
                    .execute();
        return id;
    }

    @Override
    public Article getById(long id) throws EntityNotFound {
        try {
            return this.handle
                    .createQuery("SELECT * FROM article WHERE id = :id")
                    .bind("id", id)
                    .map(
                            (rs, ctx) ->
                                    new Article(
                                            rs.getLong("id"),
                                            rs.getString("name"),
                                            rs.getString("tags")
                                    )
                    )
                    .one();
        } catch (IllegalStateException e) {
            throw new EntityNotFound("Cannot find article by id=" + id);
        }
    }

    @Override
    public List<Article> findAll() {
        return this.handle
                .createQuery("SELECT * FROM article")
                .map(
                        (rs, ctx) ->
                                new Article(
                                        rs.getLong("id"),
                                        rs.getString("name"),
                                        rs.getString("tags")
                                )
                )
                .list();
    }

    @Override
    public void delete(long id) throws EntityNotFound {
        int count = this.handle.createUpdate("DELETE FROM article WHERE id = :id").bind("id", id).execute();

        if (count == 0) {
            throw new EntityNotFound("Cannot find article by id=" + id);
        }
    }

    @Override
    public void update(Article article) throws EntityNotFound {
        int count = this.handle.createUpdate("UPDATE article SET name=:name, tags=:tags WHERE id = :id")
                .bind("id", article.getId())
                .bind("name", article.getName())
                .bind("tags", String.join(",", article.getTags()))
                .execute();

        if (count == 0) {
            throw new EntityNotFound("Cannot find article by id=" + article.getId());
        }
    }

    @Override
    public void createMultiple(List<Article> articles) {
        jdbi.inTransaction((Handle handle) -> {
            for (Article article : articles) {
                handle.createUpdate(
                                "INSERT INTO article (name, tags) VALUES (:name, :tags)")
                        .bind("name", article.getName())
                        .bind("tags", String.join(",", article.getTags()))
                        .execute();
            }
            return null;
        });
    }
}
