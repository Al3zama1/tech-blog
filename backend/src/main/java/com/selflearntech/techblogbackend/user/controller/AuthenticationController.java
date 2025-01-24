package com.selflearntech.techblogbackend.user.controller;

import com.selflearntech.techblogbackend.user.dto.UserAuthenticationRequestDTO;
import com.selflearntech.techblogbackend.user.dto.UserAuthenticationResponseDTO;
import com.selflearntech.techblogbackend.user.dto.UserDTO;
import com.selflearntech.techblogbackend.user.dto.UserRegistrationRequestDTO;
import com.selflearntech.techblogbackend.user.mapper.UserMapper;
import com.selflearntech.techblogbackend.user.service.IAuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final IAuthenticationService authenticationService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@Valid  @RequestBody UserRegistrationRequestDTO registrationPayload) {
        authenticationService.registerUser(registrationPayload);
    }

    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDTO> authenticateUser(@Valid @RequestBody UserAuthenticationRequestDTO authenticationPayload) {
        UserAuthenticationResponseDTO authenticationResponse = authenticationService.authenticateUser(authenticationPayload.getEmail(), authenticationPayload.getPassword());
        UserDTO userDTO = userMapper.toUserDTO(authenticationResponse);

        ResponseCookie refreshToken = ResponseCookie.from("refresh-token", authenticationResponse.getRefreshToken())
                .domain("localhost") // TODO: change domain for production
                .path("/api/v1/auth/refresh-token")
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
