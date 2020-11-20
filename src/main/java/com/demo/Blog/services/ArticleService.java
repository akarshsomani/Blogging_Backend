package com.demo.Blog.services;


import com.demo.Blog.dto.Article;
import com.demo.Blog.models.ArticleEntity;
import com.demo.Blog.models.TagEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public interface ArticleService {
    Article saveNewArticle(Article article);

    List<Article> findAllArticles(int pageNo);

    List<Article> findAllArticlesByTag(String tag, int pageNo);

    ResponseEntity<?> updateArticle(Article article, String slug);

    ResponseEntity<String> deleteArticle(String slug);

    List<Article> findMyArticles(int pageNo);

    Article findArticle(String slug);

    List<Article> findAllArticlesByContain(String contain, int pageNo);

    List<Article> findAllArticlesByTagAndContain(String tag, String contain, int pageNo);
}
