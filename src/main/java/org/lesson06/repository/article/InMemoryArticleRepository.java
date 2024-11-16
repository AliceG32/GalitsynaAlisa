package org.lesson06.repository.article;

import org.lesson06.repository.exception.EntityNotFound;
import org.lesson06.entity.Article;
import org.lesson06.entity.ArticleId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryArticleRepository implements ArticleRepositoryInterface {
  private final AtomicLong nextId = new AtomicLong(0);
  Map<Long, Article> data = new ConcurrentHashMap<>();

  @Override
  public synchronized long create(Article article) {
    article.setId(new ArticleId(this.nextId.incrementAndGet()));
    data.put(this.nextId.longValue(), article);

    return this.nextId.longValue();
  }

  @Override
  public Article getById(long id) throws EntityNotFound {
    if (data.containsKey(id)) {
      return data.get(id);
    } else {
      throw new EntityNotFound("Cannot find article by id=" + id);
    }
  }

  @Override
  public List<Article> findAll() {
    List<Article> list = new ArrayList<>();
    for (Map.Entry<Long, Article> entry : data.entrySet()) {
      Article article = entry.getValue();
      list.add(article);
    }

    return list;
  }

  @Override
  public void delete(long id) throws EntityNotFound {
    if (data.containsKey(id)) {
      data.remove(id);
    } else {
      throw new EntityNotFound("Cannot find article by id=" + id);
    }
  }

  @Override
  public synchronized void update(Article article) throws EntityNotFound {
    if (!data.containsKey(article.getId().getValue())) {
      throw new EntityNotFound("Cannot find article by id=" + article.getId().getValue());
    }

    data.put(article.getId().getValue(), article);
  }
}
