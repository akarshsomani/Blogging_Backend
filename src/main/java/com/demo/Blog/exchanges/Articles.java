package com.demo.Blog.exchanges;

import com.demo.Blog.dto.Article;
import com.demo.Blog.models.ArticleEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Articles {
    @NotNull
    private List<Article> articles;
}
