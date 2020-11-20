package com.demo.Blog.services;

import com.demo.Blog.dto.Article;
import com.demo.Blog.models.ArticleEntity;
import com.demo.Blog.models.ArticleTagMapEntity;
import com.demo.Blog.models.TagEntity;
import com.demo.Blog.repositoryService.ArticleRepositoryService;
import com.demo.Blog.repositoryService.ArticleTagMapRepositoryService;
import com.demo.Blog.repositoryService.TagRepositoryService;
import com.demo.Blog.util.Slug;
import com.demo.Blog.util.VariablesConfig;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Provider;

@Service
@Log4j2
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepositoryService articleRepositoryService;

    @Autowired
    private TagRepositoryService tagRepositoryService;

    @Autowired
    private ArticleTagMapRepositoryService articleTagMapRepositoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private Provider<ModelMapper> modelMapperProvider;

    VariablesConfig var = new VariablesConfig();

    Slug slugClass = new Slug();

    public void validateTagsAndMapping(String tags, long articleId) {
        List<String> tagItems = Arrays.asList(tags.split(","));
        tagItems.forEach(
                (tag) -> {
                    TagEntity tagEntity = tagRepositoryService.readSlug(slugClass.makeSlug(tag.trim()));
                    if (tagEntity == null) {
                        tagEntity = new TagEntity();
                        tagEntity.setTagName(tag.trim());
                        tagEntity.setSlug(slugClass.makeSlug(tag.trim()));
                        tagEntity = tagRepositoryService.create(tagEntity);
                    }
                    ArticleTagMapEntity articleTagMapEntity = new ArticleTagMapEntity();
                    articleTagMapEntity.setArticleId(articleId);
                    articleTagMapEntity.setTagId(tagEntity.getTagId());
                    articleTagMapEntity = articleTagMapRepositoryService.create(articleTagMapEntity);
                }
        );
        return;
    }



    public String getTagList(ArticleEntity articleEntity){
        // Getting all the mapping from the ArticleTagMap table
        List<ArticleTagMapEntity> articleTagMapping = articleTagMapRepositoryService
                .readByArticleId(articleEntity.getArticleId());

        // Fetching TagList
        String tagList = "";
        for (ArticleTagMapEntity article_tag : articleTagMapping) {
            TagEntity tagEntity = tagRepositoryService.read(article_tag.getTagId());
            // To maintain comma consistency we use the if else logic
            if (tagList == "") {
                tagList = tagList + tagEntity.getTagName();
            } else {
                tagList = tagList + ", " + tagEntity.getTagName();
            }

        }
        return tagList;
    }

    /*
    def: to map the ArticleEntity to Articles
    arg: ArticleEntity obj
    return: Article obj
     */
    public Article articleEntityToArticle(ArticleEntity articleEntity){

        String tagList = getTagList(articleEntity);

        // Creating Article object for data transfer purpose
        ModelMapper modelMapper = modelMapperProvider.get();
        Article article = modelMapper.map(articleEntity, Article.class);
        article.setTags(tagList);

        // Returning the article
        return article;

    }

    public ArticleEntity articleToArticleEntity(Article article){
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setArticleId(article.getArticleId());
        articleEntity.setHeader(article.getHeader());
        articleEntity.setBody(article.getBody());
        articleEntity.setUserId(userService.currentUser().getUserId());
        articleEntity.setSlug(slugClass.makeSlug(articleEntity.getHeader()));
        articleEntity.setModifiedDate(null);

        return articleEntity;
    }



    /*
    def: Save new article by mapping tagNames to tagId.
         Basically two interaction with Articles Database is here
         1. to save the in-coming ArticleEntity to table to generate the BlogId(ArticleId)
         2. Use that BlogId(ArticleId) to map tagId in tags table and convert tagNames with tagsId in articleEntity Obj
    arg: ArticleEntity obj
    return: Article obj
     */
    @Override
    public Article saveNewArticle(Article article) {

        ArticleEntity articleEntity = articleToArticleEntity(article);

        ArticleEntity articleEn = articleRepositoryService.createOrUpdate(articleEntity);

        if ( articleEn == null) {
            log.info("Article does not saved, check if the content is not copied or used any where else.");
            return null;
        }
        validateTagsAndMapping(article.getTags(), articleEn.getArticleId());

        log.info("Article saved successfully");
        return articleEntityToArticle(articleEn);
    }

    /*
    def: it will find all the articles without any filter
    arg:
    return:list<Article>
     */
    @Override
    public List<Article> findAllArticles(int pageNo) {

        List<Article> articleList = new ArrayList<>();

        List<ArticleEntity> articleEntityList = articleRepositoryService.readAll(pageNo);

        for (ArticleEntity articleEntity : articleEntityList) {
            articleList.add(articleEntityToArticle(articleEntity));
        }

        log.info("Articles fetched.");
        return articleList;
    }

    /*
    def: it will find all the articles with a tag
    arg: String tag
    return:list<Article>
     */
    @Override
    public List<Article> findAllArticlesByTag(String tag, int pageNo) {
        List<Article> articleList = new ArrayList<>();

        // Get TagId
        TagEntity tagEntity = tagRepositoryService.readSlug(slugClass.makeSlug(tag.trim()));

        // Return empty list if there is no such tag in the database

        if(tagEntity == null){
            log.info("There is no such tag in database.");
            return articleList;
        }

        // Get all ArticleID associated with it
        List<ArticleTagMapEntity> articleTagMapping = articleTagMapRepositoryService
                .readByTagId(tagEntity.getTagId());

        List<Long> articleIds = articleTagMapping.stream().map(ArticleTagMapEntity::getArticleId).collect(Collectors.toList());

        // Get all the ArticleEntity using ArticleId and convert to Article Entity
        List<ArticleEntity> articleEntities = articleRepositoryService.readAllByArticleIds(articleIds, pageNo);
        articleList = articleEntities.stream().map(this::articleEntityToArticle).collect(Collectors.toList());

        log.info("Articles Fetched");
        return articleList;
    }

    @Override
    public ResponseEntity<?> updateArticle(Article article, String slug) {

        ArticleEntity articleEntity = articleRepositoryService.readBySlug(slug);
        if(articleEntity == null) {
            log.info("Article does not exist, check article url and try again.");
            return ResponseEntity.ok().body(var.ARTICLE_DOES_NOT_EXIST);
        }
        if(articleEntity.getUserId() != userService.currentUser().getUserId()) {
            log.info("This article does not belong to current user");
            return ResponseEntity.ok().body(var.ARTICLE_DOES_NOT_BELONG);
        }

        articleEntity.setBody(article.getBody());
        articleEntity.setHeader(article.getHeader());
        articleEntity.setModifiedDate(null);
        articleEntity.setSlug(slugClass.makeSlug(article.getHeader()));

        ArticleEntity articleEn = articleRepositoryService.createOrUpdate(articleEntity);

        // Deleting all the previous mapping
        articleTagMapRepositoryService.deleteByArticleId(articleEn.getArticleId());
        // Revalidating all the Tags and Mappings
        validateTagsAndMapping(article.getTags(), articleEn.getArticleId());

        log.info("Article Updated");
        return ResponseEntity.ok().body(articleEntityToArticle(articleEn));


    }

    @Override
    public ResponseEntity<String> deleteArticle(String slug) {

        ArticleEntity articleEntity = articleRepositoryService.readBySlug(slug);
        if(articleEntity == null) {
            log.info("Invalid Article Slug");
            return ResponseEntity.ok().body(var.ARTICLE_DOES_NOT_EXIST);
        }
        if (articleEntity.getUserId() != userService.currentUser().getUserId()) {
            log.info("Article Does not belong to current user");
            return ResponseEntity.ok().body(var.ARTICLE_DOES_NOT_BELONG);
        }

        // Deleting all the previous mapping
        articleTagMapRepositoryService.deleteByArticleId(articleEntity.getArticleId());

        articleRepositoryService.delete(articleEntity.getArticleId());

        log.info("Article Deleted");
        return ResponseEntity.ok().body(var.ARTICLE_DELETED);

    }

    @Override
    public List<Article> findMyArticles(int pageNo) {
        List<Article> articleList = new ArrayList<>();

        List<ArticleEntity> articleEntityList =
                articleRepositoryService.readAllByUserId(userService.currentUser().getUserId(), pageNo);

        for (ArticleEntity articleEntity : articleEntityList) {
            articleList.add(articleEntityToArticle(articleEntity));
        }

        log.info("Articles for current user is fetched");
        return articleList;
    }

    @Override
    public Article findArticle(String slug) {
        ArticleEntity articleEntity= articleRepositoryService.readBySlug(slug);
        return articleEntityToArticle(articleEntity);
    }

    @Override
    public List<Article> findAllArticlesByContain(String contain, int pageNo) {

        List<ArticleEntity> articleEntities = articleRepositoryService.readByHeaderLike(contain, pageNo);
        List<Article> articleList = articleEntities.stream().map(this::articleEntityToArticle).collect(Collectors.toList());

        return articleList;
    }

    @Override
    public List<Article> findAllArticlesByTagAndContain(String tag, String contain, int pageNo) {

        List<ArticleEntity> articleEntities = articleRepositoryService.readByTagAndContain(slugClass.makeSlug(tag.trim()), contain, pageNo);
        List<Article> articleList = articleEntities.stream().map(this::articleEntityToArticle).collect(Collectors.toList());

        return articleList;
    }

}
