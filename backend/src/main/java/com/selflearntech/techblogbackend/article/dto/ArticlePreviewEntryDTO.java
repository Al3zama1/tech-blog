package com.selflearntech.techblogbackend.article.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticlePreviewEntryDTO {
    private String title;
    private AuthorDTO author;
    private String category;
    private String description;
    private Instant createdAt;
    private String coverImg;
    private String slug;
}
