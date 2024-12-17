package org.lesson08.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.lesson08.Application;
import org.lesson08.controller.response.ArticleCreateResponse;
import org.lesson08.controller.response.ArticleFindResponse;
import org.lesson08.entity.Article;
import org.lesson08.repository.article.ArticleRepositoryInterface;
import org.lesson08.repository.article.PostgresArticleRepository;
import org.lesson08.repository.comment.CommentRepositoryInterface;
import org.lesson08.repository.comment.PostgresCommentRepository;
import org.lesson08.repository.exception.EntityNotFound;
import org.lesson08.service.ArticleService;
import org.lesson08.service.CommentService;
import org.lesson08.service.exception.ArticleFindException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import spark.Service;
import org.jdbi.v3.core.Jdbi;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Set;


import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class ArticleControllerTest {
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
    void successfulArticleCreate() throws IOException, InterruptedException {
        ArticleRepositoryInterface articles = new PostgresArticleRepository(jdbi);
        CommentRepositoryInterface comments = new PostgresCommentRepository(jdbi);
        ArticleService articleService = new ArticleService(articles);
        CommentService commentService = new CommentService(articles, comments);
        ObjectMapper objectMapper = new ObjectMapper();

        Application application = new Application(
                List.of(
                        new ArticleController(
                                service,
                                articleService,
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
                                                          { "name": "articleName", "tags": ["tag1", "tag2"]}
                                                        """
                                        )
                                )
                                .uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                                .build(),
                        HttpResponse.BodyHandlers.ofString(UTF_8)
                );

        assertEquals(201, response.statusCode());
        ArticleCreateResponse articleCreateResponse =
                objectMapper.readValue(response.body(), ArticleCreateResponse.class);
        assertTrue(articleCreateResponse.id() > 0);
    }

    @Test
    void successfulArticleMultipleCreate() throws IOException, InterruptedException {
        ArticleRepositoryInterface articles = new PostgresArticleRepository(jdbi);
        CommentRepositoryInterface comments = new PostgresCommentRepository(jdbi);
        ArticleService articleService = new ArticleService(articles);
        CommentService commentService = new CommentService(articles, comments);
        ObjectMapper objectMapper = new ObjectMapper();

        Application application = new Application(
                List.of(
                        new ArticleController(
                                service,
                                articleService,
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
                                                          [{ "name": "articleName", "tags": ["tag1", "tag2"]},
                                                          { "name": "articleName", "tags": ["tag1", "tag2"]}]
                                                        """
                                        )
                                )
                                .uri(URI.create("http://localhost:%d/api/articles-multiple".formatted(service.port())))
                                .build(),
                        HttpResponse.BodyHandlers.ofString(UTF_8)
                );

        assertEquals(201, response.statusCode());
        List<Article> list = articles.findAll();
        assertEquals(2, list.size());
    }

    @Test
    void successfulGetArticle() throws IOException, InterruptedException {
        ArticleRepositoryInterface articles = new PostgresArticleRepository(jdbi);
        CommentRepositoryInterface comments = new PostgresCommentRepository(jdbi);
        ArticleService articleService = new ArticleService(articles);
        CommentService commentService = new CommentService(articles, comments);
        ObjectMapper objectMapper = new ObjectMapper();

        String articleName = "Name";
        Set<String> articleTags = Set.of("tag1", "tag2");
        Article article = new Article(null, articleName, articleTags);

        long articleId = articles.create(article);

        Application application = new Application(
                List.of(
                        new ArticleController(
                                service,
                                articleService,
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
                                .GET()
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


        assertEquals(201, response.statusCode());

        ArticleFindResponse articleFindResponse = objectMapper.readValue(response.body(), new TypeReference<>() {});

        assertEquals(articleId, articleFindResponse.getId());
        assertEquals(articleName, articleFindResponse.getName());
        assertEquals(articleTags, articleFindResponse.getTags());
    }

    @Test
    void getNotExistArticle() throws IOException, InterruptedException {
        ArticleRepositoryInterface articles = new PostgresArticleRepository(jdbi);
        CommentRepositoryInterface comments = new PostgresCommentRepository(jdbi);
        ArticleService articleService = new ArticleService(articles);
        CommentService commentService = new CommentService(articles, comments);
        ObjectMapper objectMapper = new ObjectMapper();

        String articleName = "Name";
        Set<String> articleTags = Set.of("tag1", "tag2");
        Article article = new Article(articleName, articleTags);


        Application application = new Application(
                List.of(
                        new ArticleController(
                                service,
                                articleService,
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
                                .GET()
                                .uri(
                                        URI.create(
                                                "http://localhost:%d/api/articles/%d".formatted(service.port(),
                                                        1000
                                                )
                                        )
                                )
                                .build(),
                        HttpResponse.BodyHandlers.ofString(UTF_8)
                );

        assertEquals(400, response.statusCode());
    }

    @Test
    void successfulGetArticles() throws IOException, InterruptedException {
        ArticleRepositoryInterface articles = new PostgresArticleRepository(jdbi);
        CommentRepositoryInterface comments = new PostgresCommentRepository(jdbi);
        ArticleService articleService = new ArticleService(articles);
        CommentService commentService = new CommentService(articles, comments);
        ObjectMapper objectMapper = new ObjectMapper();

        String articleName = "Name";
        Set<String> articleTags = Set.of("tag1", "tag2");
        Article article = new Article(articleName, articleTags);

        long articleId = articles.create(article);

        String articleName2 = "Name";
        Set<String> articleTags2 = Set.of("tag1", "tag2");
        Article article2 = new Article(articleName2, articleTags2);

        long articleId2 = articles.create(article2);

        Application application = new Application(
                List.of(
                        new ArticleController(
                                service,
                                articleService,
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
                                .GET()
                                .uri(
                                        URI.create(
                                                "http://localhost:%d/api/articles".formatted(service.port())
                                        )
                                )
                                .build(),
                        HttpResponse.BodyHandlers.ofString(UTF_8)
                );

        assertEquals(201, response.statusCode());

        List<ArticleFindResponse> listResponse = objectMapper.readValue(response.body(), new TypeReference<List<ArticleFindResponse>>() {
        });

        assertEquals(articleId, listResponse.get(0).getId());
        assertEquals(Set.of("tag1", "tag2"), listResponse.get(0).getTags());
        assertEquals(articleName, listResponse.get(0).getName());

        assertEquals(articleId2, (int) listResponse.get(1).getId());
        assertEquals(Set.of("tag1", "tag2"), listResponse.get(1).getTags());
        assertEquals(articleName2, listResponse.get(1).getName());
    }

    @Test
    void successfulDeleteArticle() throws IOException, InterruptedException, EntityNotFound {
        ArticleRepositoryInterface articles = new PostgresArticleRepository(jdbi);
        CommentRepositoryInterface comments = new PostgresCommentRepository(jdbi);
        ArticleService articleService = new ArticleService(articles);
        CommentService commentService = new CommentService(articles, comments);
        ObjectMapper objectMapper = new ObjectMapper();

        String articleName = "Name";
        Set<String> articleTags = Set.of("tag1", "tag2");
        Article article = new Article(articleName, articleTags);

        long articleId = articles.create(article);

        Application application = new Application(
                List.of(
                        new ArticleController(
                                service,
                                articleService,
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
                                                "http://localhost:%d/api/articles/%d".formatted(service.port(),
                                                        articleId
                                                )
                                        )
                                )
                                .build(),
                        HttpResponse.BodyHandlers.ofString(UTF_8)
                );

        EntityNotFound exception = assertThrows(
                EntityNotFound.class,
                () -> articles.getById(articleId)
        );

        assertEquals(201, response.statusCode());
        assertEquals("Cannot find article by id=" + articleId, exception.getMessage());
    }

    @Test
    void deleteNotExistsArticle() throws IOException, InterruptedException {
        ArticleRepositoryInterface articles = new PostgresArticleRepository(jdbi);
        CommentRepositoryInterface comments = new PostgresCommentRepository(jdbi);
        ArticleService articleService = new ArticleService(articles);
        CommentService commentService = new CommentService(articles, comments);
        ObjectMapper objectMapper = new ObjectMapper();

        Application application = new Application(
                List.of(
                        new ArticleController(
                                service,
                                articleService,
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
                                                "http://localhost:%d/api/articles/%d".formatted(service.port(),
                                                        1000
                                                )
                                        )
                                )
                                .build(),
                        HttpResponse.BodyHandlers.ofString(UTF_8)
                );
        assertEquals(400, response.statusCode());
    }

    @Test
    void successfulUpdateArticle() throws IOException, InterruptedException, EntityNotFound, ArticleFindException {
        ArticleRepositoryInterface articles = new PostgresArticleRepository(jdbi);
        CommentRepositoryInterface comments = new PostgresCommentRepository(jdbi);
        ArticleService articleService = new ArticleService(articles);
        CommentService commentService = new CommentService(articles, comments);
        ObjectMapper objectMapper = new ObjectMapper();

        String articleName = "Name";
        Set<String> articleTags = Set.of("tag1", "tag2");
        Article article = new Article(articleName, articleTags);

        long articleId = articles.create(article);

        Application application = new Application(
                List.of(
                        new ArticleController(
                                service,
                                articleService,
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

        assertEquals(201, response.statusCode());
        Article updatedArticle = articleService.findById(articleId);

        assertEquals("articleName2", updatedArticle.getName());
        assertEquals(Set.of("tag21", "tag11"), updatedArticle.getTags());
    }

    @Test
    void updateNotExistsArticle() throws IOException, InterruptedException, EntityNotFound, ArticleFindException {
        ArticleRepositoryInterface articles = new PostgresArticleRepository(jdbi);
        CommentRepositoryInterface comments = new PostgresCommentRepository(jdbi);
        ArticleService articleService = new ArticleService(articles);
        CommentService commentService = new CommentService(articles, comments);
        ObjectMapper objectMapper = new ObjectMapper();

        Application application = new Application(
                List.of(
                        new ArticleController(
                                service,
                                articleService,
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
                                                        1000
                                                )
                                        )
                                )
                                .build(),
                        HttpResponse.BodyHandlers.ofString(UTF_8)
                );

        assertEquals(400, response.statusCode());
    }
}
