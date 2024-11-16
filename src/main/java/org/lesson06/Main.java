package org.lesson06;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.lesson06.controller.ArticleController;
import org.lesson06.controller.ArticleFreemarkerController;
import org.lesson06.controller.CommentController;
import org.lesson06.repository.article.ArticleRepositoryInterface;
import org.lesson06.repository.article.InMemoryArticleRepository;
import org.lesson06.repository.comment.CommentRepositoryInterface;
import org.lesson06.repository.comment.InMemoryCommentRepository;
import org.lesson06.service.ArticleService;
import org.lesson06.service.CommentService;
import org.lesson06.service.template.TemplateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Service;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.*;

public class Main {
  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    Service service = Service.ignite();
    ObjectMapper objectMapper = new ObjectMapper();
    ArticleRepositoryInterface articles = new InMemoryArticleRepository();
    CommentRepositoryInterface comments = new InMemoryCommentRepository();
    FreeMarkerEngine freeMarkerEngine = TemplateFactory.freeMarkerEngine();

    Application application =
        new Application(
            List.of(
                new ArticleController(
                    service,
                    new ArticleService(articles, comments),
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
                        new ArticleService(articles, comments),
                        new CommentService(articles, comments),
                        freeMarkerEngine
                )
            )
          );
    application.start();
  }
}
