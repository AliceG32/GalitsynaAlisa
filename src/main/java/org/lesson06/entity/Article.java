package org.lesson06.entity;

import java.util.Set;

public class Article {
    private ArticleId id;
    private final String name;
    private final Set<String> tags;

    public Article(ArticleId id, String name, Set<String> tags) {
        this.id = id;
        this.name = name;
        this.tags = tags;
    }

    public Article(String name, Set<String> tags) {
        this.id = null;
        this.name = name;
        this.tags = tags;
    }

    public ArticleId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setId(ArticleId id) {
        this.id = id;
    }

    public Article withName(String newName) {
        return new Article(this.id, newName, this.tags);
    }

    public Article withTags(Set<String> tags) {
        return new Article(this.id, this.name, tags);
    }
}
