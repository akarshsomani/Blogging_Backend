package com.demo.Blog.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article{

    @Id
    private Long articleId;
    @NotNull
    private String header;
    @NotNull
    private String body;
    @NotNull
    private String tags;

    private String createdDate;

    private String modifiedDate;

    private String slug;
}
