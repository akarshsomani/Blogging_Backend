package com.demo.Blog.repository;

import com.demo.Blog.models.ArticleTagMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ArticleTagMapEntityRepository extends JpaRepository<ArticleTagMapEntity, Long> {
    public List<ArticleTagMapEntity> findByTagId(long tagId);

    public List<ArticleTagMapEntity> findByArticleId(long articleId);

    public List<ArticleTagMapEntity> deleteByArticleId(long articleId);
}
