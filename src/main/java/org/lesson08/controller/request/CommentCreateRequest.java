package org.lesson08.controller.request;

public record CommentCreateRequest(long articleId, String text) {}
