package com.selflearntech.techblogbackend.token.model;

import com.selflearntech.techblogbackend.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Entity(name = "tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(name = "refresh_token", length = 500, nullable = false)
    private String refreshToken;

    @NotNull
    @Column(name = "expire_time", nullable = false)
    private Instant expireTime;

    @Column(name = "is_valid", nullable = false)
    private boolean isValid;

    @OneToOne
    @NotNull
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
