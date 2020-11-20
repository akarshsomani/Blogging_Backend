package com.demo.Blog.repositoryService;

import com.demo.Blog.models.ArticleTagMapEntity;
import com.demo.Blog.repository.ArticleTagMapEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
public class ArticleTagMapRepositoryService {

    @Autowired
    private ArticleTagMapEntityRepository articleTagMapEntityRepository;

    public ArticleTagMapEntity create(ArticleTagMapEntity articleTagMapEntity) {

        return articleTagMapEntityRepository.save(articleTagMapEntity);
    }

    public List<ArticleTagMapEntity> readByArticleId(Long articleId){
        return articleTagMapEntityRepository.findByArticleId(articleId);
    }

    public List<ArticleTagMapEntity> readByTagId(Long tagId){
        return articleTagMapEntityRepository.findByTagId(tagId);
    }

    public List<ArticleTagMapEntity> deleteByArticleId(Long tagId){
        return articleTagMapEntityRepository.deleteByArticleId(tagId);
    }
}
