package com.demo.Blog.repository;

import com.demo.Blog.models.TagEntity;
import com.demo.Blog.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagEntityRepository extends JpaRepository<TagEntity, Long> {
    public TagEntity findByTagName(String tagName);

    public TagEntity findByTagId(long tagId);

    public TagEntity findBySlug(String slug);
}
