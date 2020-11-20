package com.demo.Blog.repositoryService;

import com.demo.Blog.models.UserEntity;
import com.demo.Blog.repository.UserEntityRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
@Log4j2
public class UserRepositoryService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    public UserEntity createOrUpdate(UserEntity userEntity) {
        try{
            userEntity = userEntityRepository.save(userEntity);
        }
        catch (DataIntegrityViolationException e) {
            log.info("Username {} already exist", userEntity.getUsername());
            return null;
        }
        return userEntity;
    }

    public UserEntity readByUserName(String userName){
        return userEntityRepository.findByUsername(userName);
    }

    public void deleteUser(long userId){
        userEntityRepository.deleteById(userId);
    }
}
