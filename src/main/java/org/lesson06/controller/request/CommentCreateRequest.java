package org.lesson06.controller.request;

public record CommentCreateRequest(long articleId, String text) {}
