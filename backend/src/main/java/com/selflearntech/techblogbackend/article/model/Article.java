package com.selflearntech.techblogbackend.article.model;

import com.selflearntech.techblogbackend.user.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article {

    private String id;
    @NotBlank
    private String title;
    @Indexed(unique = true)
    private String slug;
    private String content;
    private String description;
    private Image coverImg;
    private String category;
    private boolean isFeatured = false;
    private Integer visits = 0;
    @DBRef
    private User user;
    @NotNull
    private Instant createdAt;
    private Instant updatedAt;


}
