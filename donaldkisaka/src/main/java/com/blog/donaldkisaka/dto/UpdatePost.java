package com.blog.donaldkisaka.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePost {
    @NotBlank(message = "Title is required")
    @Size(max = 200)
    private String title;

    @NotBlank(message = "Content is required")
    @Size(max = 10000)
    private String content;

    private Boolean published;

}
