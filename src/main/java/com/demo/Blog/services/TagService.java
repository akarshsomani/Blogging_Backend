package com.demo.Blog.services;

import com.demo.Blog.models.TagEntity;
import com.demo.Blog.repositoryService.TagRepositoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class TagService {

    @Autowired
    private TagRepositoryService tagRepositoryService;


    public ResponseEntity<?> getTags() {

        List<TagEntity> tagList = new ArrayList<>();

        tagList = tagRepositoryService.readAll();

        log.info("Fetched all Tags");
        return ResponseEntity.ok().body(tagList);
    }
}
