package com.selflearntech.techblogbackend.user.controller;

import com.selflearntech.techblogbackend.user.dto.AuthenticationRequestDTO;
import com.selflearntech.techblogbackend.user.dto.AuthenticationResponseDTO;
import com.selflearntech.techblogbackend.user.dto.UserDTO;
import com.selflearntech.techblogbackend.user.dto.RegistrationRequestDTO;
import com.selflearntech.techblogbackend.user.mapper.UserMapper;
import com.selflearntech.techblogbackend.user.service.IAuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Duration;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final IAuthenticationService authenticationService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> registerUser(@Valid  @RequestBody RegistrationRequestDTO payload) {
        String userId = authenticationService.registerUser(payload);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create(String.format("/api/v1/users/%s", userId)))
                .build();
    }

    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDTO> authenticateUser(@Valid @RequestBody AuthenticationRequestDTO payload) {
        AuthenticationResponseDTO authenticationResponse = authenticationService.authenticateUser(payload.getEmail(), payload.getPassword());
        UserDTO userDTO = userMapper.toUserDTO(authenticationResponse);

        ResponseCookie refreshToken = ResponseCookie.from("refresh-token", authenticationResponse.getRefreshToken())
                .domain("localhost") // TODO: change domain for production
                .path("/api/v1/auth/refresh-access")
                .httpOnly(true)
                .maxAge(Duration.ofDays(7))
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, refreshToken.toString())
                .body(userDTO);
    }

    @PostMapping("/refresh-access")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO refreshAccess(@CookieValue(name = "refresh-token") String refreshToken) {
        return authenticationService.refreshAccessToken(refreshToken);
    }
}
