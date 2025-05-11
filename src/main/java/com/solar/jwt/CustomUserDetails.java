package com.solar.jwt;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.solar.dto.AccountType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {
    private String id;
    private String username;
    private String name;
    private String password;
    private AccountType accountType;  // Representing the role
    private Collection<? extends GrantedAuthority> authorities;
}
