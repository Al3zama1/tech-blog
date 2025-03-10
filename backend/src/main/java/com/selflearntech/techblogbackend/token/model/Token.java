package com.selflearntech.techblogbackend.token.model;

import com.selflearntech.techblogbackend.user.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {

    @Id
    private String id;
    @NotBlank
    private String refreshToken;
    @NotNull
    private Instant expireTime;
    private boolean isValid;
    @DBRef
    private User user;
}
