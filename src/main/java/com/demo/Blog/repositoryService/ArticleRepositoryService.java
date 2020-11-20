package com.demo.Blog.repositoryService;


import com.demo.Blog.dto.Article;
import com.demo.Blog.models.ArticleEntity;
import com.demo.Blog.repository.ArticleEntityRepository;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;


@Service
@Log4j2
public class ArticleRepositoryService {

    @Autowired
    private ArticleEntityRepository articleEntityRepository;

    private int pageSize = 5;

    public ArticleEntity createOrUpdate(ArticleEntity articleEntity) {
        try{
            articleEntity = articleEntityRepository.save(articleEntity);
        }
        catch (DataIntegrityViolationException e) {
            log.info("Duplicates Found, A similar Article exists");
            return null;
        }
        return articleEntity;
    }

    public void delete(long articleId) {
        articleEntityRepository.deleteById(articleId);
    }

    public List<ArticleEntity> readAll(int pageNo) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("createdDate").descending());
        return articleEntityRepository.findAll(paging).toList();
    }

    public ArticleEntity read(Long articleId) {
        return articleEntityRepository.findByArticleId(articleId);
    }

    public List<ArticleEntity> readAllByUserId(Long userId, int pageNo) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("createdDate").descending());
        return articleEntityRepository.findByUserId(userId, paging);
    }

    public List<ArticleEntity> readAllByArticleIds(List<Long> articleIds, int pageNo) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("createdDate").descending());
        return articleEntityRepository.findByArticleIdIn(articleIds, paging);
    }

    public ArticleEntity readBySlug(String slug) {
        return articleEntityRepository.findBySlug(slug);
    }

    public List<ArticleEntity> readByHeaderLike(String contain, int pageNo) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("createdDate").descending());
        return articleEntityRepository.findByHeaderContaining(contain, paging);
    }

    public List<ArticleEntity> readByTagAndContain(String tag, String contain, int pageNo) {

        // created_date because it is naive query
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("A.created_date").descending());
        return articleEntityRepository.findByTagAndContain(tag, contain, paging);
    }
}
