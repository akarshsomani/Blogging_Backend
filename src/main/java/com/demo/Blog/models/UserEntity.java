package com.demo.Blog.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String email;

    @CreatedDate
    @Column(nullable = false)
    private Date createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private Date modifiedDate;

    @Column(nullable = false)
    private Date passwordModifiedDate;

}
