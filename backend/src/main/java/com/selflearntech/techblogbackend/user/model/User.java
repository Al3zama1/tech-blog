package com.selflearntech.techblogbackend.user.model;

import com.selflearntech.techblogbackend.article.model.Article;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    private String id;
    String firstName;
    String lastName;
    @Indexed(unique = true)
    private String email;
    private String password;
    private String introduction;
    private String linkedInUrl;
    private String gitHubUrl;
    private String profileImg;
    private Instant createdAt;
    private Set<Article> savedArticles = new HashSet<>();
    @DBRef()
    private Set<Role> authorities = new HashSet<>();

    @Override
    public String getUsername() {
        return email;
    }
}
