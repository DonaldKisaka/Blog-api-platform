package com.blog.donaldkisaka.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatePost {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    private Boolean published = false;

}
