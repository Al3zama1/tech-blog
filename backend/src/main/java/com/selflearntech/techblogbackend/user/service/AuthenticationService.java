package com.selflearntech.techblogbackend.user.service;

import com.selflearntech.techblogbackend.exception.*;
import com.selflearntech.techblogbackend.token.model.Token;
import com.selflearntech.techblogbackend.token.repository.TokenRepository;
import com.selflearntech.techblogbackend.token.service.TokenService;
import com.selflearntech.techblogbackend.user.dto.UserAuthenticationResponseDTO;
import com.selflearntech.techblogbackend.user.dto.UserDTO;
import com.selflearntech.techblogbackend.user.dto.UserRegistrationRequestDTO;
import com.selflearntech.techblogbackend.user.mapper.UserMapper;
import com.selflearntech.techblogbackend.user.model.Role;
import com.selflearntech.techblogbackend.user.model.RoleType;
import com.selflearntech.techblogbackend.user.model.User;
import com.selflearntech.techblogbackend.user.repository.RoleRepository;
import com.selflearntech.techblogbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Transactional
@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService{

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserMapper userMapper;
    private final Clock clock;

    @Override
    public void registerUser(UserRegistrationRequestDTO registerDTO) {
        if (!registerDTO.getPassword().equals(registerDTO.getVerifyPassword())) throw new BadRequestException(ErrorMessages.PASSWORDS_MUST_MATCH);
        boolean emailTaken = userRepository.existsUserByEmail(registerDTO.getEmail());
        if (emailTaken) throw new UserExistsException(ErrorMessages.EMAIL_TAKEN);

        Role userRole = roleRepository.findByAuthority(RoleType.USER)
                .orElseThrow(() -> new RoleAssignmentException(ErrorMessages.ROLE_ASSIGNMENT_FAILURE, RoleType.USER.name()));
        String encodedPassword = passwordEncoder.encode(registerDTO.getPassword());

        User newUser = User.builder()
                .firstName(registerDTO.getFirstName())
                .lastName(registerDTO.getLastName())
                .email(registerDTO.getEmail())
                .authorities(Set.of(userRole))
                .password(encodedPassword)
                .build();

        userRepository.save(newUser);
    }

    @Override
    public UserAuthenticationResponseDTO authenticateUser(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        User authenticatedUser = (User) authentication.getPrincipal();
        Token token = Token.builder()
                .user(authenticatedUser)
                .isValid(true)
                .build();
        token = tokenRepository.save(token);

        String accessToken = tokenService.createAccessToken(authenticatedUser);
        Instant refreshTokenExpiration = Instant.now(clock).plus(7, ChronoUnit.DAYS);
        String refreshToken = tokenService.createRefreshToken(authenticatedUser.getEmail(), token.getId(), refreshTokenExpiration);

        token.setRefreshToken(refreshToken);
        token.setExpireTime(refreshTokenExpiration);
        tokenRepository.save(token);

        return userMapper.toUserAuthenticationResponseDTO(authenticatedUser, refreshToken, accessToken);
    }

    @Override
    public UserDTO refreshAccessToken(String refreshToken) {
        Jwt decodedJwt;

        try {
            decodedJwt = tokenService.validateJWT(refreshToken);
        } catch (JwtException ex) {
            throw new RefreshTokenException(ErrorMessages.FAILED_TOKEN_DECODE);
        }

        Token storedRefreshToken = tokenRepository.findById(decodedJwt.getClaim("id"))
                .orElseThrow(() -> new RefreshTokenException(ErrorMessages.REFRESH_TOKEN_NOT_FOUND));
        Instant now = Instant.now(clock);

        if (!storedRefreshToken.getUser().getEmail().equals(decodedJwt.getSubject())) throw new RefreshTokenException(ErrorMessages.STORED_TOKEN_AND_COOKIE_REFRESH_TOKEN_USER_MISMATCH);
        if (!storedRefreshToken.isValid()) throw new RefreshTokenException(ErrorMessages.INVALIDATED_REFRESH_TOKEN);
        if (!storedRefreshToken.getRefreshToken().equals(refreshToken)) throw new RefreshTokenException(ErrorMessages.COOKIE_REFRESH_TOKEN_AND_DB_TOKEN_MISMATCH);
        if (storedRefreshToken.getExpireTime().isBefore(now)) throw new RefreshTokenException(ErrorMessages.EXPIRED_REFRESH_TOKEN);

        String newAccessToken = tokenService.createAccessToken(storedRefreshToken.getUser());
        return userMapper.toUserDTO(storedRefreshToken.getUser(), newAccessToken);
    }
}
