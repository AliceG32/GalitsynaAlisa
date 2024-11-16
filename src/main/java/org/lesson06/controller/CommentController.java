package org.lesson06.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import org.lesson06.controller.request.ArticleCreateRequest;
import org.lesson06.controller.request.ArticleUpdateRequest;
import org.lesson06.controller.request.CommentCreateRequest;
import org.lesson06.controller.response.*;
import org.lesson06.entity.Article;
import org.lesson06.service.ArticleService;
import org.lesson06.service.CommentService;
import org.lesson06.service.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Service;

public class CommentController implements Controller {
    private static final Logger LOG = LoggerFactory.getLogger(CommentController.class);

    private final Service service;
    private final ObjectMapper objectMapper;
    private final CommentService commentService;

    public CommentController(
            Service service, CommentService commentService, ObjectMapper objectMapper) {
        this.service = service;
        this.commentService = commentService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void initializeEndpoints() {
        createComment();
        delete();
    }

    private void createComment() {
        service.post(
                "/api/comments",
                (Request request, Response response) -> {
                    response.type("application/json");
                    String body = request.body();
                    CommentCreateRequest commentCreateRequest =
                            objectMapper.readValue(body, CommentCreateRequest.class);
                    long commentId;
                    try {
                        commentId = commentService.create(commentCreateRequest.articleId(), commentCreateRequest.text());
                        LOG.debug("Created comment with id {}", commentId);
                        response.status(201);
                        return objectMapper.writeValueAsString(new CommentCreateResponse(commentId));
                    } catch (CommentCreateException e) {
                        LOG.warn("Article not found.", e);
                        response.status(400);
                        return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
                    } catch (Throwable e) {
                        LOG.error(e.getMessage(), e);
                        response.status(500);
                        return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
                    }
                }
        );
    }

    private void delete() {
        service.delete(
                "/api/comments/:id",
                (Request request, Response response) -> {
                    response.type("application/json");
                    try {
                        commentService.delete(Long.parseLong(request.params(":id")));
                        LOG.debug("Deleted comment with id {}", request.params(":id"));
                        response.status(201);
                        return objectMapper.writeValueAsString("Ok");
                    } catch (CommentDeleteException e) {
                        LOG.warn("Comment not found.", e);
                        response.status(400);
                        return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
                    } catch (Throwable e) {
                        LOG.error(e.getMessage(), e);
                        response.status(500);
                        return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
                    }
                }
        );
    }
}
