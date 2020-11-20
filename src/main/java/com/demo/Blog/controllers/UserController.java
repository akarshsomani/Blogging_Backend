package com.demo.Blog.controllers;

import com.demo.Blog.models.UserEntity;
import com.demo.Blog.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Log4j2
@RequestMapping("/users")
public class UserController {


    @Autowired
    private UserService userService;


    private static final String NEW = "/new";
    private static final String UPDATE = "/update";
    private static final String DELETE = "/delete";
    private static final String USERNAME = "/{username}";


    // Create a new User
    @PostMapping(NEW)
    public ResponseEntity<String> createUser(@RequestBody UserEntity userEntity) {
        log.info("Started user creation.");
        return userService.createUser(userEntity);
    }

    // Update a User
    @PutMapping(USERNAME + UPDATE)
    public ResponseEntity<?> updateUser(@RequestBody UserEntity userEntity, @PathVariable String username) {
        log.info("Started updating a user.");
        return userService.updateUserDetails(userEntity, username);
    }

    // get User
    @GetMapping(USERNAME)
    public ResponseEntity<?> getUser(@PathVariable String username) {
        log.info("Started getting current user details.");
        return userService.getUserDetails(username);
    }

    // delete a User
    @DeleteMapping(USERNAME + DELETE)
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        log.info("Started deleting user - {}",username);
        return userService.deleteUser(username);
    }


}