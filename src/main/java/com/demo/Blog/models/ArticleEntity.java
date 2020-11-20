package com.demo.Blog.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@JsonIgnoreProperties(value = {"createdDate", "updatedDate"}, ignoreUnknown = true)
@Data
@Table(name = "articles")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class ArticleEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    @NotNull
    @Column(unique = true)
    private String header;

    @NotNull
    @Column(unique = true)
    private String body;


    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(nullable = false)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @Column(nullable = false)
    private Date modifiedDate;

    @Column(nullable = false)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String slug;

}