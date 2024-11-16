package org.lesson06.repository.comment;

import org.lesson06.entity.Article;
import org.lesson06.entity.ArticleId;
import org.lesson06.entity.Comment;
import org.lesson06.repository.exception.EntityNotFound;

import java.util.List;

public interface CommentRepositoryInterface {
    long create(Comment comment);
    List<Comment> findAllByArticleId(long id);
    void delete(long id) throws EntityNotFound;
    void deleteByArticleId(long articleId);
    long getCountByArticleId(long id);
}
