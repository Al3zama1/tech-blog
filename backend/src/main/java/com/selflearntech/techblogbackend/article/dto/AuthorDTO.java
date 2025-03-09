package com.selflearntech.techblogbackend.article.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String introduction;
    private String profileImg;
    private String linkedInUrl;
    private String gitHubUrl;
}
