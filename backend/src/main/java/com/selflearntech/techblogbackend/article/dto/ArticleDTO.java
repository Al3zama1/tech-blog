package com.selflearntech.techblogbackend.article.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {
    private String title;
    private String content;
    private String coverImg;
    private String description;
    private String category;
    private AuthorDTO author;
    private boolean isFeatured;
    private Instant createdAt;
}
