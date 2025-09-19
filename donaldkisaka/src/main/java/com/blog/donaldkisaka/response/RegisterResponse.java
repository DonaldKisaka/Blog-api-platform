package com.blog.donaldkisaka.response;

import lombok.Data;

@Data
public class RegisterResponse {
    private String token;
    private long expiresIn;
    private String username;
    private String email;

    public RegisterResponse(String token, long expiresIn, String username, String email) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.username = username;
        this.email = email;
    }
}
