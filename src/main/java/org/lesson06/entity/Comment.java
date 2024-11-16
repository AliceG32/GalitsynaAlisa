package org.lesson06.entity;

public class Comment {
    private CommentId id;
    private ArticleId articleId;
    String text;

    public Comment(ArticleId articleId, String text) {
        this.id = null;
        this.articleId = articleId;
        this.text = text;
    }

    public CommentId getId() {
        return id;
    }

    public ArticleId getArticleId() {
        return articleId;
    }

    public String getText() {
        return text;
    }

    public void setId(CommentId id) {
        this.id = id;
    }
}
