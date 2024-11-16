package org.lesson06.controller.request;

import java.util.Set;

public record ArticleCreateRequest(String name, Set<String> tags) {}
