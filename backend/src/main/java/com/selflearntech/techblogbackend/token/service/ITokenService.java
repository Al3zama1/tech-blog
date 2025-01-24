package com.selflearntech.techblogbackend.token.service;

import com.selflearntech.techblogbackend.user.model.User;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;

public interface ITokenService {

    String createAccessToken(User user);

    String createRefreshToken(String subject, Instant expiresAt);

    Jwt validateJWT(String token);
}
