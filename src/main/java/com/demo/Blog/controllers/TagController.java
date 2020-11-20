package com.demo.Blog.controllers;

import com.demo.Blog.dto.Article;
import com.demo.Blog.exchanges.Articles;
import com.demo.Blog.services.ArticleService;
import com.demo.Blog.services.TagService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private ArticleService articleService;

    private static final String TAGS = "/tags";


    // get Tags
    @GetMapping()
    public ResponseEntity<?> getTags() {
        log.info("Started getting all the tags.");
        return tagService.getTags();
    }

    //get Tags related articles
    @GetMapping("/{tagname}")
    public ResponseEntity<?> getArticlesRelatedToTags(@PathVariable String tagname
            , @RequestParam(name  = "page_no", defaultValue = "1") int pageNo) {

        pageNo = pageNo - 1;

        List<Article> articleList = new ArrayList<Article>();
        articleList.addAll(articleService.findAllArticlesByTag(tagname, pageNo));

        Articles articles = new Articles();
        articles.setArticles(articleList);

        return ResponseEntity.ok().body(articles);
    }

}