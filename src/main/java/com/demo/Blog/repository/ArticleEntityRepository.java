package com.demo.Blog.repository;

import com.demo.Blog.models.ArticleEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleEntityRepository extends PagingAndSortingRepository<ArticleEntity, Long> {

    ArticleEntity findByArticleId(Long blogId);

    List<ArticleEntity> findByUserId(Long userId, Pageable paging);

    List<ArticleEntity> findByArticleIdIn(List<Long> articleIds, Pageable paging);

    ArticleEntity findBySlug(String slug);

    List<ArticleEntity> findByHeaderContaining(String contain, Pageable paging);

  @Query(
      value =
          "select distinct A.* from article_tag_map AT left join tags T on AT.tag_id = T.tag_id\n"
              + "left join articles A on AT.article_id = A.article_id\n"
              + "where  T.slug = :#{#tag} or A.header like %:contain%",
      countQuery =
          "select count distinct A.* from article_tag_map AT left join tags T on AT.tag_id = T.tag_id\n"
              + "left join articles A on AT.article_id = A.article_id\n"
              + "where  T.slug = :#{#tag} or A.header like %:contain%",
      nativeQuery = true)
  List<ArticleEntity> findByTagAndContain(
      @Param("tag") String tag, String contain, Pageable pageable);
}
