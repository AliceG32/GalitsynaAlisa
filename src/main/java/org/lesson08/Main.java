package org.lesson08;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.jdbi.v3.core.Jdbi;
import org.lesson08.controller.ArticleController;
import org.lesson08.controller.ArticleFreemarkerController;
import org.lesson08.controller.CommentController;
import org.lesson08.repository.article.ArticleRepositoryInterface;
import org.lesson08.repository.article.PostgresArticleRepository;
import org.lesson08.repository.comment.CommentRepositoryInterface;
import org.lesson08.repository.comment.PostgresCommentRepository;
import org.lesson08.service.ArticleService;
import org.lesson08.service.CommentService;
import org.lesson08.service.MigrateService;
import org.lesson08.service.template.TemplateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Service;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.*;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Config config = ConfigFactory.load();
        MigrateService migrateService = new MigrateService(
                config.getString("app.database.url"),
                config.getString("app.database.user"),
                config.getString("app.database.password")
        );
        migrateService.migrate();

        Jdbi jdbi = Jdbi.create(
                config.getString("app.database.url"),
                config.getString("app.database.user"),
                config.getString("app.database.password")
        );

        Service service = Service.ignite();
        ObjectMapper objectMapper = new ObjectMapper();
        ArticleRepositoryInterface articles = new PostgresArticleRepository(jdbi);
        CommentRepositoryInterface comments = new PostgresCommentRepository(jdbi);
        FreeMarkerEngine freeMarkerEngine = TemplateFactory.freeMarkerEngine();

        Application application =
                new Application(
                        List.of(
                                new ArticleController(
                                        service,
                                        new ArticleService(articles),
                                        new CommentService(articles, comments),
                                        objectMapper
                                ),
                                new CommentController(
                                        service,
                                        new CommentService(articles, comments),
                                        objectMapper
                                ),
                                new ArticleFreemarkerController(
                                        service,
                                        new ArticleService(articles),
                                        new CommentService(articles, comments),
                                        freeMarkerEngine
                                )
                        )
                );
        application.start();
    }
}
