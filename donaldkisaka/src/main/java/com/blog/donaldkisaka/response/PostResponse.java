package com.blog.donaldkisaka.response;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        String title,
        String content,
        AuthorInfo author,
        Boolean published,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}