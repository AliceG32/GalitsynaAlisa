package org.lesson06.controller;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lesson06.Application;
import org.lesson06.entity.Article;
import org.lesson06.repository.article.ArticleRepositoryInterface;
import org.lesson06.repository.article.InMemoryArticleRepository;
import org.lesson06.repository.comment.CommentRepositoryInterface;
import org.lesson06.repository.comment.InMemoryCommentRepository;
import org.lesson06.service.ArticleService;
import org.lesson06.service.CommentService;
import org.lesson06.service.template.TemplateFactory;
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

class ArticleFreemarkerControllerTest {
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
  void successfulGetArticles() throws IOException, InterruptedException {
    ArticleRepositoryInterface articles = new InMemoryArticleRepository();
    CommentRepositoryInterface comments = new InMemoryCommentRepository();
    ArticleService articleService = new ArticleService(articles, comments);
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
