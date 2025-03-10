package com.selflearntech.techblogbackend.article.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticlePreviewPageDTO {
    List<ArticlePreviewEntryDTO> articles;
    boolean hasMorePages;
}
