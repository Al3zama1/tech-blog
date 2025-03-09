package com.selflearntech.techblogbackend.article.model;

import com.selflearntech.techblogbackend.user.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Draft {

    private String id;
    private String title;
    private String content;
    private String description;
    private Image coverImg;
    private String category = "general";
    @DBRef
    private Article article;
    @DBRef
    private User user;
    @NotNull
    private Instant createdAt;
    @NotNull
    private Instant updatedAt;
}
