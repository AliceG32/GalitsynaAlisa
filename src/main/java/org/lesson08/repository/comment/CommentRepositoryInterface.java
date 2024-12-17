package org.lesson08.repository.comment;

import org.lesson08.entity.Comment;
import org.lesson08.repository.exception.EntityNotFound;

import java.util.List;

public interface CommentRepositoryInterface {
    long nextId();
    long create(Comment comment);
    List<Comment> findAllByArticleId(long id);
    void delete(long id) throws EntityNotFound;
    long getCountByArticleId(long id);
}
