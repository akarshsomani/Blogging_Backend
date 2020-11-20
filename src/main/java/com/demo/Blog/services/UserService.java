package com.demo.Blog.services;

import com.demo.Blog.dto.Article;
import com.demo.Blog.dto.User;
import com.demo.Blog.util.VariablesConfig;
import com.demo.Blog.models.UserEntity;
import com.demo.Blog.repositoryService.UserRepositoryService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Provider;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Log4j2
public class UserService {

    @Autowired
    private UserRepositoryService userRepositoryService;

    @Autowired
    private Provider<ModelMapper> modelMapperProvider;

    VariablesConfig var = new VariablesConfig();

    public UserEntity findByUsername(String userName){
        return userRepositoryService.readByUserName(userName);
    }

    public UserEntity currentUser() {
        String currentUserName;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        currentUserName = authentication.getName();
        return findByUsername(currentUserName);
    }

    public boolean passwordValidator(String password) {

        // check if number is there
        boolean case1 = password.matches(".*\\d.*");

        // check if alphabet is there
        boolean case2 = password.matches(".*[a-zA-Z]+.*");

        // check if alphabet is there
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(password);
        boolean case3 = m.find();

        //check if length is greater than 8 and less than 15
        boolean case4 = password.length() < 15 && password.length() >=8;

        if (case1 && case2 && case3 && case4) {

            return true;
        }
        else {
            return false;
        }

    }

    public boolean validateUsername(String username) {
        return username.matches("[a-z0-9]+");
    }

    public ResponseEntity<String> createUser(UserEntity userEntity){

        userEntity.setUsername(userEntity.getUsername().toLowerCase());

        if (userEntity.getPasswordModifiedDate() == null){
            userEntity.setPasswordModifiedDate(new Date());
        }

        //check if the username is valid
        boolean usernameValidity = validateUsername(userEntity.getUsername());
        if(usernameValidity == false) {
            log.info("Bad Username");
            return ResponseEntity.badRequest().body("Bad Username, make sure username only contains alphabets and digits. \nAlso it is not case sensitive");
        }


        // check for password validity
        boolean validity = passwordValidator(userEntity.getPassword());

        if (validity ) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            if (userRepositoryService.createOrUpdate(userEntity) != null) {
                log.info("User created");
                return ResponseEntity.ok().body(var.USER_CREATED);
            }
            log.info("Username already exist");
            return ResponseEntity.badRequest().body(var.USERNAME_EXIST);
        }
        else{
            log.info("Password Strength Week");
            return  ResponseEntity.badRequest().body(var.PASSWORD_STRENGTH_WEEK);
        }
    }

    public ResponseEntity<?> updateUserDetails(UserEntity userEntity, String username) {

        username = username.toLowerCase();
        userEntity.setUsername(userEntity.getUsername().toLowerCase());

        UserEntity userEntity_database_record = currentUser();

        // check if database record and url username matches
        if (!userEntity_database_record.getUsername().equals(username)) {
            log.info("Username in the URL do not match with the current username");
            return ResponseEntity.badRequest().body("Username in the URL do not match with the current username");
        }

        //check if the username is valid
        boolean usernameValidity = validateUsername(userEntity.getUsername());
        if(usernameValidity == false) {
            log.info("Bad Username");
            return ResponseEntity.badRequest().body("Bad Username, make sure username only contains alphabets and digits. \nAlso it is not case sensitive");
        }

        // change password modified date if the passoword is modified
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(!passwordEncoder.matches(userEntity.getPassword(), userEntity_database_record.getPassword())) {
            if(passwordValidator(userEntity.getPassword()))
            {
                userEntity_database_record.setPasswordModifiedDate(new Date());
                userEntity_database_record.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            }
            else {
                log.info("Password Strength Week");
                return  ResponseEntity.badRequest().body(var.PASSWORD_STRENGTH_WEEK);
            }
        }


        userEntity_database_record.setUsername(userEntity.getUsername());
        userEntity_database_record.setEmail(userEntity.getEmail());
        userEntity_database_record.setModifiedDate(null);


        userEntity_database_record = userRepositoryService.createOrUpdate(userEntity_database_record);
        if( userEntity_database_record == null)
        {
            log.info("Try another username, this is already used");
            return ResponseEntity.ok().body(var.USER_UPDATED);
        }
        else {
            log.info("User Updated");
            return ResponseEntity.ok().body(var.USER_UPDATED);
        }
    }


    public ResponseEntity<?> getUserDetails(String username) {
        UserEntity userEntity = userRepositoryService.readByUserName(username.toLowerCase());

        if(userEntity == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        ModelMapper modelMapper = modelMapperProvider.get();
        User user = modelMapper.map(userEntity, User.class);
        log.info("User detail returned.");
        return ResponseEntity.ok().body(user);
    }

    public ResponseEntity<String> deleteUser(String username) {
        username = username.toLowerCase();
        UserEntity userEntity = currentUser();
        if (!userEntity.getUsername().equals(username)) {
            log.info("current username and provided username do not match");
            return ResponseEntity.badRequest().body("Username is incorrect");
        }
        userRepositoryService.deleteUser(userEntity.getUserId());
        log.info("User Deleted.");
        return ResponseEntity.ok().body(var.USER_DELETED);

    }
}
