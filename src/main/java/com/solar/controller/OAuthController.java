package com.solar.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthController {

    @GetMapping("/")
    public String welcome() {
        return "Welcome to OAuth2 Login App";
    }

    @GetMapping("/profile")
    public String userProfile(@AuthenticationPrincipal OidcUser user) {
        return "Logged in as: " + user.getFullName();
    }
}
