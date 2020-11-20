package com.demo.Blog.repositoryService;

import com.demo.Blog.models.TagEntity;
import com.demo.Blog.repository.TagEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
public class TagRepositoryService {

    @Autowired
    private TagEntityRepository tagEntityRepository;

    public TagEntity create(TagEntity tagEntity) {
        return tagEntityRepository.save(tagEntity);
    }

    public TagEntity read(Long tagId) {
        return tagEntityRepository.findByTagId(tagId);
    }

    public TagEntity read(String tagName) {
        return tagEntityRepository.findByTagName(tagName);
    }

    public List<TagEntity> readAll() {
        return tagEntityRepository.findAll();
    }

    public TagEntity readSlug(String slug) {
        return tagEntityRepository.findBySlug(slug);
    }
}
