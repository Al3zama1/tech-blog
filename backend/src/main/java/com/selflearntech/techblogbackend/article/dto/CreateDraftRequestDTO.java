package com.selflearntech.techblogbackend.article.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDraftRequestDTO {
    @Size(min = 24, max = 24)
    private String authorId;
}
