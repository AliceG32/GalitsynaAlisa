package org.lesson06.controller.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lesson06.Application;
import org.lesson06.controller.ArticleController;
import org.lesson06.controller.CommentController;
import org.lesson06.controller.response.ArticleCreateResponse;
import org.lesson06.controller.response.CommentCreateResponse;
import org.lesson06.entity.Article;
import org.lesson06.entity.Comment;
import org.lesson06.repository.article.ArticleRepositoryInterface;
import org.lesson06.repository.article.InMemoryArticleRepository;
import org.lesson06.repository.comment.CommentRepositoryInterface;
import org.lesson06.repository.comment.InMemoryCommentRepository;
import org.lesson06.repository.exception.EntityNotFound;
import org.lesson06.service.ArticleService;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

public class E2ETest {
  private Service service;
  ObjectMapper objectMapper;

  @BeforeEach
  void beforeEach() {
    service = Service.ignite();
    objectMapper = new ObjectMapper();
  }

  @AfterEach
  void afterEach() {
    service.stop();
    service.awaitStop();
  }

  @Test
  void e2eTest() throws IOException, InterruptedException, EntityNotFound {
    ArticleRepositoryInterface articles = new InMemoryArticleRepository();
    CommentRepositoryInterface comments = new InMemoryCommentRepository();
    ArticleService articleService = new ArticleService(articles, comments);
    CommentService commentService = new CommentService(articles, comments);

    Application application = new Application(
            List.of(
                    new ArticleController(
                            service,
                            articleService,
                            commentService,
                            objectMapper
                    ),
                    new CommentController(
                            service,
                            commentService,
                            objectMapper
                    )
            )
    );

    application.start();
    service.awaitInitialization();

  List<Comment> articleComments;

    long articleId = createArticle();
    articleComments = comments.findAllByArticleId(articleId);
    assertEquals(0, articleComments.size());
    long commentId = addComment(articleId);
    articleComments = comments.findAllByArticleId(articleId);
    assertEquals(1, articleComments.size());
    updateArticle(articleId);
    deleteComment(commentId);
    articleComments = comments.findAllByArticleId(articleId);
    assertEquals(0, articleComments.size());

    Article article = articles.getById(articleId);
    assertEquals("articleName2", article.getName());
    assertEquals(Set.of("tag11", "tag21"), article.getTags());
  }

  private void deleteComment(long commentId) throws IOException, InterruptedException {
    HttpResponse<String> response = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .DELETE()
                            .uri(
                                    URI.create(
                                            "http://localhost:%d/api/comments/%d".formatted(service.port(), commentId)
                                    )
                            )
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );
  }

  private void updateArticle(long articleId) throws IOException, InterruptedException {
    HttpResponse<String> response = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .PUT(
                                    HttpRequest.BodyPublishers.ofString(
                                            """
                                                    { "name": "articleName2", "tags": ["tag11", "tag21"]}
                                                  """
                                    )
                            )
                            .uri(
                                    URI.create(
                                            "http://localhost:%d/api/articles/%d".formatted(service.port(),
                                                    articleId
                                            )
                                    )
                            )
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );
  }

  private long addComment(long articleId) throws IOException, InterruptedException {
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

    CommentCreateResponse commentCreateResponse =
            objectMapper.readValue(response.body(), CommentCreateResponse.class);

    return commentCreateResponse.id();
  }


  private long createArticle() throws IOException, InterruptedException {
    HttpResponse<String> response = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .POST(
                                    HttpRequest.BodyPublishers.ofString(
                                            """
                                                    { "name": "articleName", "tags": ["tag1", "tag2"]}
                                                  """
                                    )
                            )
                            .uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    ArticleCreateResponse articleCreateResponse =
            objectMapper.readValue(response.body(), ArticleCreateResponse.class);
    return  articleCreateResponse.id();
  }
}
