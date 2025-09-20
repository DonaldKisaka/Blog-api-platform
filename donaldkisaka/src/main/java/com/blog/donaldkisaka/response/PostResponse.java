package com.blog.donaldkisaka.response;

import com.blog.donaldkisaka.model.User;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        String title,
        String content,
        User author,
        Boolean published,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}