package org.lesson06.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lesson06.Application;
import org.lesson06.controller.response.CommentCreateResponse;
import org.lesson06.entity.Article;
import org.lesson06.entity.ArticleId;
import org.lesson06.entity.Comment;
import org.lesson06.repository.article.ArticleRepositoryInterface;
import org.lesson06.repository.article.InMemoryArticleRepository;
import org.lesson06.repository.comment.CommentRepositoryInterface;
import org.lesson06.repository.comment.InMemoryCommentRepository;
import org.lesson06.service.CommentService;
import spark.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

class CommentControllerTest {
  private Service service;

  @BeforeEach
  void beforeEach() {
    service = Service.ignite();
  }

  @AfterEach
  void afterEach() {
    service.stop();
    service.awaitStop();
  }

  @Test
  void createNotExistsArticle() throws IOException, InterruptedException {
    ArticleRepositoryInterface articles = new InMemoryArticleRepository();
    CommentRepositoryInterface comments = new InMemoryCommentRepository();
    CommentService commentService = new CommentService(articles, comments);
    ObjectMapper objectMapper = new ObjectMapper();

    Application application = new Application(
            List.of(
                    new CommentController(
                            service,
                            commentService,
                            objectMapper
                    )
            )
    );

    application.start();
    service.awaitInitialization();

    HttpResponse<String> response = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .POST(
                                    HttpRequest.BodyPublishers.ofString(
                                            """
                                                    { "articleId": 1000, "text": "comment"}
                                                  """
                                    )
                            )
                            .uri(URI.create("http://localhost:%d/api/comments".formatted(service.port())))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(400, response.statusCode());
  }

  @Test
  void successfulCommentCreate() throws IOException, InterruptedException {
    ArticleRepositoryInterface articles = new InMemoryArticleRepository();
    CommentRepositoryInterface comments = new InMemoryCommentRepository();
    CommentService commentService = new CommentService(articles, comments);
    ObjectMapper objectMapper = new ObjectMapper();

    String articleName = "Name";
    Set<String> articleTags = Set.of("tag1", "tag2");
    Article article = new Article(articleName, articleTags);

    long articleId = articles.create(article);

    Application application = new Application(
            List.of(
                    new CommentController(
                            service,
                            commentService,
                            objectMapper
                    )
            )
    );

    application.start();
    service.awaitInitialization();

    HttpResponse<String> response = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .POST(
                                    HttpRequest.BodyPublishers.ofString(
                                            """
                                                    { "articleId": 1, "text": "comment"}
                                                  """
                                    )
                            )
                            .uri(URI.create("http://localhost:%d/api/comments".formatted(service.port())))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(201, response.statusCode());
    CommentCreateResponse commentCreateResponse =
            objectMapper.readValue(response.body(), CommentCreateResponse.class);
    assertTrue(commentCreateResponse.id() > 0);
  }

  @Test
  void successfulCommentDelete() throws IOException, InterruptedException {
    ArticleRepositoryInterface articles = new InMemoryArticleRepository();
    CommentRepositoryInterface comments = new InMemoryCommentRepository();
    CommentService commentService = new CommentService(articles, comments);
    ObjectMapper objectMapper = new ObjectMapper();

    String articleName = "Name";
    Set<String> articleTags = Set.of("tag1", "tag2");
    Article article = new Article(articleName, articleTags);

    long articleId = articles.create(article);
    comments.create(new Comment(new ArticleId(articleId), "comment"));

    Application application = new Application(
            List.of(
                    new CommentController(
                            service,
                            commentService,
                            objectMapper
                    )
            )
    );

    application.start();
    service.awaitInitialization();

    HttpResponse<String> response = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .DELETE()
                            .uri(
                                  URI.create(
                                    "http://localhost:%d/api/comments/%d".formatted(service.port(), 1)
                                  )
                            )
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(201, response.statusCode());
  }

  @Test
  void deleteNotExistsComment() throws IOException, InterruptedException {
    ArticleRepositoryInterface articles = new InMemoryArticleRepository();
    CommentRepositoryInterface comments = new InMemoryCommentRepository();
    CommentService commentService = new CommentService(articles, comments);
    ObjectMapper objectMapper = new ObjectMapper();

    Application application = new Application(
            List.of(
                    new CommentController(
                            service,
                            commentService,
                            objectMapper
                    )
            )
    );

    application.start();
    service.awaitInitialization();

    HttpResponse<String> response = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .DELETE()
                            .uri(
                                    URI.create(
                                            "http://localhost:%d/api/comments/%d".formatted(service.port(), 1000)
                                    )
                            )
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(400, response.statusCode());
  }
}
