package org.lesson08.controller;

import org.lesson08.entity.Article;
import org.lesson08.service.ArticleService;
import org.lesson08.service.CommentService;
import spark.ModelAndView;
import spark.Service;
import spark.template.freemarker.FreeMarkerEngine;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleFreemarkerController implements Controller {
  private final Service service;
  private final ArticleService articleService;
  private final CommentService commentService;
  private final FreeMarkerEngine freeMarkerEngine;

  public ArticleFreemarkerController(
          Service service,
          ArticleService articleService,
          CommentService commentService,
          FreeMarkerEngine freeMarkerEngine
  ) {
    this.service = service;
    this.articleService = articleService;
    this.commentService = commentService;
    this.freeMarkerEngine = freeMarkerEngine;
  }

  @Override
  public void initializeEndpoints() {
    getAllArticles();
  }

  private void getAllArticles() {
    service.get(
            "/",
            (Request request, Response response) -> {
              response.type("text/html; charset=utf-8");
              List<Article> articles = articleService.findAll();
              List<Map<String, String>> articlesList = new ArrayList<>();
              for (Article article : articles) {
                Long commentCount = commentService.getCountByArticleId(article.getId());
                articlesList.add(Map.of("name", article.getName(), "commentCount", commentCount.toString()));
              }

              Map<String, Object> model = new HashMap<>();
              model.put("articles", articlesList);
              return freeMarkerEngine.render(new ModelAndView(model, "index.ftl"));
            }
    );
  }
}
