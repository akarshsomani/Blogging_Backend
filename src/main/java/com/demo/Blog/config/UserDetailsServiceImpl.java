package com.demo.Blog.config;

import com.demo.Blog.models.UserEntity;
import com.demo.Blog.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        UserEntity userEntity = userEntityRepository.findByUsername(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return new MyUserDetails(userEntity);
    }

}
