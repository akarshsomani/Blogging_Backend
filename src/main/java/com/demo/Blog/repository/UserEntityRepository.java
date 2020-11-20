package com.demo.Blog.repository;

import com.demo.Blog.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    public UserEntity findByUsername(String Username);


}
