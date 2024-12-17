package org.lesson08.entity;

import org.jetbrains.annotations.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class Article {
    @Nullable
    private final Long id;
    private final String name;
    private final Set<String> tags;

    public Article(@Nullable Long id, String name, Set<String> tags) {
        this.id = id;
        this.name = name;
        this.tags = tags;
    }

    public Article(@Nullable Long id, String name, String tags) {
        this.id = id;
        this.name = name;
        this.tags = Arrays.stream(tags.split(",")).collect(Collectors.toSet());
    }

    public Article(String name, Set<String> tags) {
        this.id = null;
        this.name = name;
        this.tags = tags;
    }

    public @Nullable Long  getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<String> getTags() {
        return tags;
    }

    public Article withName(String newName) {
        return new Article(this.id, newName, this.tags);
    }

    public Article withTags(Set<String> tags) {
        return new Article(this.id, this.name, tags);
    }
}
