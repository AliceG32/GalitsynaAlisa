package org.lesson08.controller;

import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lesson08.Application;
import org.lesson08.entity.Article;
import org.lesson08.repository.article.ArticleRepositoryInterface;
import org.lesson08.repository.article.PostgresArticleRepository;
import org.lesson08.repository.comment.CommentRepositoryInterface;
import org.lesson08.repository.comment.PostgresCommentRepository;
import org.lesson08.service.ArticleService;
import org.lesson08.service.CommentService;
import org.lesson08.service.template.TemplateFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import spark.Service;
import spark.template.freemarker.FreeMarkerEngine;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class ArticleFreemarkerControllerTest {
  @Container
  public static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:14");

  private Service service;
  private static Jdbi jdbi;

  @BeforeAll
  static void beforeAll() {
    String postgresJdbcUrl = POSTGRES.getJdbcUrl();
    Flyway flyway =
            Flyway.configure()
                    .outOfOrder(true)
                    .locations("classpath:db/migrations")
                    .dataSource(postgresJdbcUrl, POSTGRES.getUsername(), POSTGRES.getPassword())
                    .load();
    flyway.migrate();
    jdbi = Jdbi.create(postgresJdbcUrl, POSTGRES.getUsername(), POSTGRES.getPassword());
  }

  @BeforeEach
  void beforeEach() {
    service = Service.ignite();
    jdbi.useTransaction(handle -> handle.createUpdate("DELETE FROM article").execute());
  }

  @AfterEach
  void afterEach() {
    service.stop();
    service.awaitStop();
  }

  @Test
  void successfulGetArticles() throws IOException, InterruptedException {
    ArticleRepositoryInterface articles = new PostgresArticleRepository(jdbi);
    CommentRepositoryInterface comments = new PostgresCommentRepository(jdbi);
    ArticleService articleService = new ArticleService(articles);
    CommentService commentService = new CommentService(articles, comments);
    FreeMarkerEngine freeMarkerEngine = TemplateFactory.freeMarkerEngine();

    String articleName = "Name";
    Set<String> articleTags = Set.of("tag1", "tag2");
    Article article = new Article(articleName, articleTags);

    long articleId = articles.create(article);

    String articleName2 = "Name2";
    Set<String> articleTags2 = Set.of("tag11", "tag22");
    Article article2 = new Article(articleName2, articleTags2);

    long articleId2 = articles.create(article2);

    Application application = new Application(
            List.of(
                    new ArticleFreemarkerController(
                            service,
                            articleService,
                            commentService,
                            freeMarkerEngine
                    )
            )
    );

    application.start();
    service.awaitInitialization();

    HttpResponse<String> response = HttpClient.newHttpClient()
            .send(
                    HttpRequest.newBuilder()
                            .GET()
                            .uri(
                                    URI.create(
                                            "http://localhost:%d/".formatted(service.port())
                                    )
                            )
                            .build(),
                    HttpResponse.BodyHandlers.ofString(UTF_8)
            );

    assertEquals(200, response.statusCode());
    assertThat(response.body()).contains("<td>%s</td>".formatted(articleName));
    assertThat(response.body()).contains("<td>%s</td>".formatted(articleName2));
  }
}
