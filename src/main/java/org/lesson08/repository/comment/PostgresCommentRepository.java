package org.lesson08.repository.comment;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.lesson08.entity.Article;
import org.lesson08.entity.Comment;
import org.lesson08.repository.exception.EntityNotFound;

import java.util.List;

public class PostgresCommentRepository implements CommentRepositoryInterface {

    private final Handle handle;
    private final Jdbi jdbi;

    public PostgresCommentRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
        this.handle = jdbi.open();
    }

    @Override
    public long nextId() {
        return (long) this.handle.createQuery("SELECT nextval('comment_id_seq') AS value")
                .mapToMap()
                .first()
                .get("value");
    }

    @Override
    public long create(Comment comment) {
        return jdbi.inTransaction((Handle handle) -> {
            long id = nextId();
            handle.createQuery("SELECT id FROM article WHERE id = :article_id FOR UPDATE")
                    .bind("article_id", comment.getArticleId())
                    .mapToMap()
                    .first();

            handle.createUpdate("INSERT INTO comment (id, text, article_id) VALUES (:id, :text, :article_id)")
                    .bind("id", id)
                    .bind("text", comment.getText())
                    .bind("article_id", comment.getArticleId())
                    .execute();

            handle.createUpdate(
                            "UPDATE article SET trending=(select count(id) from comment where article_id = :article_id) > 3 WHERE id = :article_id"
                    )
                    .bind("article_id", comment.getArticleId())
                    .execute();

            return id;
        });
    }

    @Override
    public List<Comment> findAllByArticleId(long id) {
        return this.handle
                .createQuery("SELECT * FROM comment WHERE article_id = :article_id")
                .bind("article_id", id)
                .map(
                        (rs, ctx) ->
                                new Comment(
                                        rs.getLong("id"),
                                        id,
                                        rs.getString("text")
                                )
                )
                .list();
    }

    @Override
    public void delete(long id) throws EntityNotFound {
        try {
            jdbi.inTransaction((Handle handle) -> {
                Comment comment = handle
                        .createQuery("SELECT * FROM comment WHERE id = :id")
                        .bind("id", id)
                        .map(
                                (rs, ctx) ->
                                        new Comment(
                                                rs.getLong("id"),
                                                rs.getLong("article_id"),
                                                rs.getString("text")
                                        )
                        )
                        .one();
                handle.createUpdate("DELETE FROM comment WHERE id = :id").bind("id", id).execute();

                handle.createUpdate(
                                "UPDATE article SET trending=(select count(id) from comment where article_id = :article_id) > 3 WHERE id = :article_id"
                        )
                        .bind("article_id", comment.getArticleId())
                        .execute();
                return id;
            });
        } catch (IllegalStateException e) {
            throw new EntityNotFound("Cannot find comment by id=" + id);
        }
    }

    @Override
    public long getCountByArticleId(long id) {
        return (long) this.handle.createQuery("SELECT count(id) AS value FROM comment WHERE article_id = :article_id")
                .bind("article_id", id)
                .mapToMap()
                .first()
                .get("value");
    }
}
