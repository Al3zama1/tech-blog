package com.selflearntech.techblogbackend.article.dto;

import com.selflearntech.techblogbackend.article.model.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DraftDTO {
    private String title;
    private String content;
    private String description;
    private Image coverImg;
    private String category;

    public void setCategory(String category) {
        this.category = category != null ? category.replace("-", " ") : "technology";
    }
}
