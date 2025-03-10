package com.selflearntech.techblogbackend.utils;

import com.selflearntech.techblogbackend.token.model.Token;

public class TokenMother {

    public static Token.TokenBuilder token() {
        return Token.builder()
                .id("64f57b8a18c4a1a5f9f8c671")
                .isValid(true)
                .refreshToken("refresh-token");
    }
}
