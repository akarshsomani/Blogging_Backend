package com.demo.Blog.controllers;

import com.demo.Blog.dto.Article;
import com.demo.Blog.exchanges.Articles;
import com.demo.Blog.services.ArticleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@Log4j2
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;


    private static final String ARTICLE = "/articles";
    private static final String MY_ARTICLE = "/myarticles";
    private static final String NEW = "/new";
    private static final String SEARCH = "/search";
    private static final String UPDATE = "/update";
    private static final String DELETE = "/delete";
    private static final String TAG = "tag";
    private static final String CONTAIN = "contain";
    private static final String PAGE_NO = "page_no";
    private static final String SLUG = "/{slug}";


    // Create a new Article
    @PostMapping(NEW)
    public ResponseEntity<?> createArticle(@Valid @RequestBody Article article) {
        log.info("Started creating a new article");
        article = articleService.saveNewArticle(article);

        if (article == null){
            return ResponseEntity.badRequest().body("Article not saved, make sure the content is not copied.");
        }
        return ResponseEntity.ok().body(article);
    }

    // Update an Article
    @PutMapping(SLUG+UPDATE)
    public ResponseEntity<?> updateArticle(@Valid @RequestBody Article article, @PathVariable String slug) {
        log.info("Started Updating an article.");
        return articleService.updateArticle(article, slug);
    }

    // Delete an Article
    @DeleteMapping(SLUG+DELETE)
    public ResponseEntity<String> deleteArticle(@PathVariable String slug) {
        log.info("Started deleting an article.");
        return articleService.deleteArticle(slug);
    }

    // Get all articles
    @GetMapping()
    public ResponseEntity<Articles> fetchArticles(@RequestParam(name = PAGE_NO , defaultValue = "1") int pageNo) {
        pageNo = pageNo - 1;

        log.info("Started Fetching all articles");
        Articles articles = new Articles();
        articles.setArticles(articleService.findAllArticles(pageNo));

        return ResponseEntity.ok().body(articles);
    }

    // Get all articles by tag
    @GetMapping(SEARCH)
    public ResponseEntity<?> fetchArticlesByTagAndContains(
            @RequestParam(name  = PAGE_NO, defaultValue = "1") int pageNo,
            @RequestParam(name = TAG, required = false) String tag,
            @RequestParam(name = CONTAIN, required = false) String contain) {
        pageNo = pageNo - 1;

        if(tag == null && contain == null) {
            return ResponseEntity.badRequest().body("No parameter provided for search");
        }

        log.info("Started searching articles by tag.");

        List<Article> articleList = new ArrayList<Article>();
        if( tag != null) {
            articleList.addAll(articleService.findAllArticlesByTag(tag, pageNo));
        }
        if( contain != null) {
            articleList.addAll(articleService.findAllArticlesByContain(contain, pageNo));
        }

        if( tag != null && contain != null) {

            Set<Article> s= new HashSet<Article>();
            s.addAll(articleList);
            articleList = new ArrayList<Article>();
            articleList.addAll(s);

            Collections.sort(articleList, new Comparator<Article>() {

                public int compare(Article o1, Article o2) {
                    // compare two instance of `Score` and return `int` as result.
                    return o2.getCreatedDate().compareTo(o1.getCreatedDate());
                }
            });


        }

        Articles articles = new Articles();
        articles.setArticles(articleList);
        return ResponseEntity.ok().body(articles);
    }

    // Get all articles by tag
    @GetMapping("/v2" + SEARCH)
    public ResponseEntity<?> fetchArticlesByTagAndContainsV2(
            @RequestParam(name  = PAGE_NO, defaultValue = "1") int pageNo,
            @RequestParam(name = TAG, required = false) String tag,
            @RequestParam(name = CONTAIN, required = false) String contain) {
        pageNo = pageNo - 1;

        if(tag == null && contain == null) {
            return ResponseEntity.badRequest().body("No parameter provided for search");
        }

        log.info("Started searching articles by tag.");

        List<Article> articleList = new ArrayList<Article>();
        if( tag != null && contain == null) {
            articleList.addAll(articleService.findAllArticlesByTag(tag, pageNo));
        }
        if( contain != null && tag == null) {
            articleList.addAll(articleService.findAllArticlesByContain(contain, pageNo));
        }

        if( tag != null && contain != null) {

            articleList.addAll(articleService.findAllArticlesByTagAndContain(tag, contain, pageNo));
        }

        Articles articles = new Articles();
        articles.setArticles(articleList);
        return ResponseEntity.ok().body(articles);
    }

    // Get my articles.
    @GetMapping(MY_ARTICLE)
    public ResponseEntity<Articles> getMyArticles(@RequestParam(name = PAGE_NO, defaultValue = "1") int pageNo) {
        pageNo = pageNo - 1;

        log.info("Started fetching my articles.");
        Articles articles = new Articles();

        articles.setArticles(articleService.findMyArticles(pageNo));
        return ResponseEntity.ok().body(articles);
    }

    // Get an articles.
    @GetMapping(SLUG)
    public ResponseEntity<Article> getArticle(@PathVariable String slug) {
        String message = "Started fetching article - "+ slug;
        log.info(message);
        Article article = articleService.findArticle(slug);
        if(article == null) {
            ResponseEntity.badRequest().body("Article not found");
        }
        return ResponseEntity.ok().body(article);
    }
}