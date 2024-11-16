package org.lesson06.repository.comment;

import org.lesson06.entity.ArticleId;
import org.lesson06.entity.Comment;
import org.lesson06.entity.CommentId;
import org.lesson06.repository.exception.EntityNotFound;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryCommentRepository implements CommentRepositoryInterface {
    private final AtomicLong nextId = new AtomicLong(0);
    Map<Long, Comment> data = new ConcurrentHashMap<>();

    @Override
    public synchronized long create(Comment comment) {
        comment.setId(new CommentId(this.nextId.incrementAndGet()));
        data.put(this.nextId.longValue(), comment);
        return this.nextId.longValue();
    }

    @Override
    public List<Comment> findAllByArticleId(long articleId) {
        List<Comment> list = new ArrayList<>();
        for (Map.Entry<Long, Comment> entry : data.entrySet()) {
            Comment comment = entry.getValue();
            if (comment.getArticleId().getValue() == articleId) {
                list.add(comment);
            }
        }

        return list;
    }

    @Override
    public void delete(long id) throws EntityNotFound {
        if (data.containsKey(id)) {
            data.remove(id);
        } else {
            throw new EntityNotFound("Cannot find comment by id=" + id);
        }
    }

    @Override
    public void deleteByArticleId(long articleId) {
        for (Map.Entry<Long, Comment> entry : data.entrySet()) {
            Long key = entry.getKey();
            Comment comment = entry.getValue();
            if (comment.getArticleId().getValue() == articleId) {
                data.remove(key);
            }
        }
    }

    @Override
    public long getCountByArticleId(long articleId) {
        long result = 0;
        for (Map.Entry<Long, Comment> entry : data.entrySet()) {
            Comment comment = entry.getValue();
            if (comment.getArticleId().getValue() == articleId) {
                result++;
            }
        }

        return result;
    }
}
