package com.solar.jwt;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.solar.dto.UserDto;
import com.solar.service.UserService;

@Service
public class MyUserDetailService implements UserDetailsService {
    

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto user;
        try {
            user = userService.getUserByEmail(username);
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found with username: " + username, e);
        }
        return new CustomUserDetails(user.getUserId(), username, user.getName(), user.getUserPassword(), user.getRole(), new ArrayList<>());
    }

    
}
