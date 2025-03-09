package com.selflearntech.techblogbackend.article.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    private String url;
    private String thumbnailUrl;
    private String filePath;
    private String name;
    private String fileId;
}
