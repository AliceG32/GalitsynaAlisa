package org.lesson08.entity;

import org.jetbrains.annotations.Nullable;

public class Comment {
    private final Long id;
    private final Long articleId;
    String text;

    public Comment(@Nullable Long id, Long articleId, String text) {
        this.id = id;
        this.articleId = articleId;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public Long getArticleId() {
        return articleId;
    }

    public String getText() {
        return text;
    }
}
