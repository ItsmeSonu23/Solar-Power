package com.solar.jwt;

import lombok.Data;

@Data
public class AuthenticationResponse {
    final private String jwt;
    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }
}
