package org.lesson06.controller.request;

import java.util.Set;

public record ArticleUpdateRequest(String name, Set<String> tags) {}
