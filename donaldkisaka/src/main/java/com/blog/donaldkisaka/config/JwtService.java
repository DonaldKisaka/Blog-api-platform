package com.blog.donaldkisaka.config;

import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private String secretKey;

    private long jwtExpirationMs;
}
