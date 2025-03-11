package com.selflearntech.techblogbackend.article.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublishDraftRequestDTO {
    @Size(min = 24, max = 24, message = "Author id must be 24 characters long")
    private String authorId;
}
