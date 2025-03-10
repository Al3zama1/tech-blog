package com.selflearntech.techblogbackend.user.service;

import com.selflearntech.techblogbackend.exception.*;
import com.selflearntech.techblogbackend.token.model.Token;
import com.selflearntech.techblogbackend.token.repository.TokenRepository;
import com.selflearntech.techblogbackend.token.service.TokenService;
import com.selflearntech.techblogbackend.utils.UserMother;
import com.selflearntech.techblogbackend.user.dto.AuthenticationRequestDTO;
import com.selflearntech.techblogbackend.user.dto.RegistrationRequestDTO;
import com.selflearntech.techblogbackend.user.mapper.UserMapper;
import com.selflearntech.techblogbackend.user.model.Role;
import com.selflearntech.techblogbackend.user.model.RoleType;
import com.selflearntech.techblogbackend.user.model.User;
import com.selflearntech.techblogbackend.user.repository.RoleRepository;
import com.selflearntech.techblogbackend.user.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private Clock clock;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenService tokenService;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthenticationService cut;

    @Nested
    class UserRegistration {

        @Test
        void registerUser_WithValidData_ShouldCreateNewUser() {
            // Given
            RegistrationRequestDTO registrationPayload = UserMother.registrationPayload().build();
            Role userRole = Role.builder().authority(RoleType.USER).build();
            String encodedPassword = "encoded-password";
            LocalDateTime defaultLocalDateTime = LocalDateTime.of(2024, 12, 13, 12, 15);
            Clock fixedClock = Clock.fixed(defaultLocalDateTime.toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
            Instant currentTime = fixedClock.instant();

            given(userRepository.existsUserByEmail(registrationPayload.getEmail())).willReturn(false);
            given(roleRepository.findByAuthority(RoleType.USER)).willReturn(Optional.of(userRole));
            given(passwordEncoder.encode(registrationPayload.getPassword())).willReturn(encodedPassword);
            given(clock.instant()).willReturn(currentTime);
            given(userRepository.save(any(User.class))).willAnswer(result -> {
                User user = result.getArgument(0);
                user.setId("64c3e9f2a1b2c34d56ef1234");
                return user;
            });

            // When
            cut.registerUser(registrationPayload);

            // Then
            ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
            then(userRepository).should().save(userArgumentCaptor.capture());

            User savedUser = userArgumentCaptor.getValue();
            assertThat(savedUser.getPassword()).isEqualTo(encodedPassword);
            assertThat(savedUser.getFirstName()).isEqualTo(registrationPayload.getFirstName());
            assertThat(savedUser.getLastName()).isEqualTo(registrationPayload.getLastName());
            assertThat(savedUser.getEmail()).isEqualTo(registrationPayload.getEmail());
            assertThat(savedUser.getAuthorities().size()).isEqualTo(1);
            assertThat(savedUser.getAuthorities().stream().findFirst().get().getAuthority()).isEqualTo(RoleType.USER.name());
            assertThat(savedUser.getCreatedAt()).isEqualTo(currentTime);
        }

        @Test
        void registerUser_WithNonMatchingPasswords_ShouldThrowBadRequestException() {
            // Given
            RegistrationRequestDTO registrationPayload = UserMother.registrationPayload()
                    .password("c11l9u")
                    .build();

            // When
            assertThatThrownBy(() -> cut.registerUser(registrationPayload))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage(ErrorMessages.PASSWORDS_MUST_MATCH);

            // Then
            then(userRepository).shouldHaveNoInteractions();
            then(roleRepository).shouldHaveNoInteractions();
            then(passwordEncoder).shouldHaveNoInteractions();
        }

        @Test
        void registerUser_WithEmailAlreadyTaken_ShouldThrowConflictException() {
            // Given
            RegistrationRequestDTO registrationPayload = UserMother.registrationPayload().build();

            given(userRepository.existsUserByEmail(registrationPayload.getEmail())).willReturn(true);

            // When
            assertThatThrownBy(() -> cut.registerUser(registrationPayload))
                    .isInstanceOf(ConflictException.class)
                            .hasMessage(ErrorMessages.EMAIL_TAKEN);

            // Then
            then(userRepository).should(never()).save(any(User.class));
            then(roleRepository).shouldHaveNoInteractions();
            then(passwordEncoder).shouldHaveNoInteractions();
        }

        @Test
        void registerUser_FailToAssignUserRole_ShouldThrowRoleAssignmentException() {
            // Given
            RegistrationRequestDTO registrationPayload = UserMother.registrationPayload().build();

            given(userRepository.existsUserByEmail(registrationPayload.getEmail())).willReturn(false);
            given(roleRepository.findByAuthority(RoleType.USER)).willReturn(Optional.empty());

            // When
            assertThatThrownBy(() -> cut.registerUser(registrationPayload))
                    .isInstanceOf(RoleAssignmentException.class)
                    .hasMessage(ErrorMessages.ROLE_ASSIGNMENT_FAILURE + ": " + RoleType.USER.name());

            // Then
            then(passwordEncoder).shouldHaveNoInteractions();
            then(userRepository).should(never()).save(any(User.class));

        }
    }

    @Nested
    class UserAuthentication {
        @Test
        void authenticateUser_WithValidCredentials_ShouldReturnAuthenticationResponseDTO() {
            // Given
            AuthenticationRequestDTO authenticationPayload = UserMother.authenticationPayload().build();
            User authenticatedUser = UserMother.user().build();
            UsernamePasswordAuthenticationToken authRequestToken = new UsernamePasswordAuthenticationToken(authenticationPayload.getEmail(), authenticationPayload.getPassword());
            UsernamePasswordAuthenticationToken authResponseToken = new UsernamePasswordAuthenticationToken(authenticatedUser, null);

            LocalDateTime defaultLocalDateTime = LocalDateTime.of(2024, 12, 13, 12, 15);
            Clock fixedClock = Clock.fixed(defaultLocalDateTime.toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
            Instant currentTime = fixedClock.instant();

            Token token = Token.builder().user(authenticatedUser).isValid(true).build();
            String accessToken = "access-token";
            String refreshToken = "refresh-token";
            String refreshTokenId = "refresh-token-id";
            Instant refreshTokenExpiration = fixedClock.instant().plus(7, ChronoUnit.DAYS);

            given(authenticationManager.authenticate(authRequestToken)).willReturn(authResponseToken);
            given(tokenRepository.save(token)).willAnswer(invocation -> {
                Token savedToken = invocation.getArgument(0, Token.class);
                savedToken.setId(refreshTokenId);
                return savedToken;
            });
            given(tokenService.createAccessToken(authenticatedUser)).willReturn(accessToken);
            given(clock.instant()).willReturn(currentTime);
            given(tokenService.createRefreshToken(authenticatedUser.getEmail(), refreshTokenId, refreshTokenExpiration)).willReturn(refreshToken);


            // When
            cut.authenticateUser(authenticationPayload.getEmail(), authenticationPayload.getPassword());

            // Then
            ArgumentCaptor<Token> tokenArgumentCaptor = ArgumentCaptor.forClass(Token.class);
            then(tokenRepository).should(times(2)).save(tokenArgumentCaptor.capture());

            token = tokenArgumentCaptor.getAllValues().get(1);
            assertThat(token).isNotNull();
            assertThat(token.getId()).isEqualTo(refreshTokenId);
            assertThat(token.getRefreshToken()).isEqualTo(refreshToken);
            assertThat(token.getExpireTime()).isEqualTo(refreshTokenExpiration);
            assertThat(token.getUser()).isEqualTo(authenticatedUser);
            assertThat(token.isValid()).isTrue();

            then(userMapper).should().toAuthenticationResponseDTO(authenticatedUser, refreshToken, accessToken);
        }
    }


    @Nested
    class RefreshAccessToken {

        @Test
        void refreshAccessToken_WithValidToken_ShouldReturnNewAccessToken() {
            // Given
            LocalDateTime defaultLocalDateTime = LocalDateTime.of(2024, 12, 13, 12, 15);
            Clock fixedClock = Clock.fixed(defaultLocalDateTime.toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
            Instant currentTime = fixedClock.instant();
            Instant refreshTokenExpiration = fixedClock.instant().plus(7, ChronoUnit.DAYS);

            String refreshToken = "refresh-token";
            String accessToken = "access-token";
            Token token = Token.builder()
                    .id("refresh-token-id")
                    .refreshToken(refreshToken)
                    .expireTime(refreshTokenExpiration)
                    .isValid(true)
                    .user(UserMother.user().build())
                    .build();

            Jwt jwt = Jwt.withTokenValue(refreshToken)
                    .expiresAt(refreshTokenExpiration)
                    .subject(token.getUser().getEmail())
                    .claim("id", token.getId())
                    .header("alg", "SH252")
                    .build();

            given(tokenService.validateJWT(refreshToken)).willReturn(jwt);
            given(tokenRepository.findById(token.getId())).willReturn(Optional.of(token));
            given(clock.instant()).willReturn(currentTime);
            given(tokenService.createAccessToken(token.getUser())).willReturn(accessToken);

            // When
            cut.refreshAccessToken(refreshToken);

            // Then
            then(tokenService).should().createAccessToken(token.getUser());
            then(userMapper).should().toUserDTO(token.getUser(), accessToken);
        }

        @Test
        void refreshAccessToken_WithTokenValidationException_ShouldThrowRefreshTokenException() {
            // Given
            String refreshToken = "refresh-token";

            given(tokenService.validateJWT(refreshToken)).willThrow(new JwtException(ErrorMessages.INVALID_REFRESH_TOKEN));

            // When
            assertThatThrownBy(() -> cut.refreshAccessToken(refreshToken))
                    .isInstanceOf(RefreshTokenException.class)
                    .hasMessage(ErrorMessages.FAILED_TOKEN_VALIDATION);

            // Then
            then(userRepository).shouldHaveNoInteractions();
            then(tokenService).shouldHaveNoMoreInteractions();
            then(userMapper).shouldHaveNoInteractions();
        }

        @Test
        void refreshAccessToken_WithNonMatchingTokenId_ShouldThrowRefreshTokenException() {
            // Given

            String refreshToken = "refresh-token";
            String subject = "john.doe@gmail.com";
            Jwt jwt = Jwt.withTokenValue(refreshToken)
                    .subject(subject)
                    .claim("id", "refresh-token-id")
                    .header("alg", "SH252")
                    .build();

            given(tokenService.validateJWT(refreshToken)).willReturn(jwt);
            given(tokenRepository.findById(jwt.getClaim("id"))).willReturn(Optional.empty());

            // When
            assertThatThrownBy(() -> cut.refreshAccessToken(refreshToken))
                    .isInstanceOf(RefreshTokenException.class)
                    .hasMessage(ErrorMessages.REFRESH_TOKEN_NOT_FOUND);

            // Then
            then(tokenService).shouldHaveNoMoreInteractions();
            then(userMapper).shouldHaveNoInteractions();
        }

        @Test
        void refreshAccessToken_WithCookieAndStoredRefreshTokenUserMismatch_ShouldThrowRefreshTokenException() {
            // Given
            LocalDateTime defaultLocalDateTime = LocalDateTime.of(2024, 12, 13, 12, 15);
            Clock fixedClock = Clock.fixed(defaultLocalDateTime.toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
            Instant currentTime = fixedClock.instant();
            Instant refreshTokenExpiration = fixedClock.instant().plus(7, ChronoUnit.DAYS);

            String refreshToken = "refresh-token";
            Token token = Token.builder()
                    .id("refresh-token-id")
                    .refreshToken(refreshToken)
                    .expireTime(refreshTokenExpiration)
                    .isValid(true)
                    .user(UserMother.user().build())
                    .build();

            Jwt jwt = Jwt.withTokenValue(refreshToken)
                    .expiresAt(refreshTokenExpiration)
                    .subject("john@gmail.com")
                    .claim("id", token.getId())
                    .header("alg", "SH252")
                    .build();

            given(tokenService.validateJWT(refreshToken)).willReturn(jwt);
            given(tokenRepository.findById(token.getId())).willReturn(Optional.of(token));
            given(clock.instant()).willReturn(currentTime);

            // When
            assertThatThrownBy(() -> cut.refreshAccessToken(refreshToken))
                    .isInstanceOf(RefreshTokenException.class)
                    .hasMessage(ErrorMessages.STORED_TOKEN_AND_COOKIE_REFRESH_TOKEN_USER_MISMATCH);

            // Then
            then(tokenService).shouldHaveNoMoreInteractions();
            then(userMapper).shouldHaveNoInteractions();
        }

        @Test
        void refreshAccessToken_WithInvalidatedToken_ShouldThrowRefreshTokenException() {
            // Given
            LocalDateTime defaultLocalDateTime = LocalDateTime.of(2024, 12, 13, 12, 15);
            Clock fixedClock = Clock.fixed(defaultLocalDateTime.toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
            Instant currentTime = fixedClock.instant();
            Instant refreshTokenExpiration = fixedClock.instant().plus(7, ChronoUnit.DAYS);

            String refreshToken = "refresh-token";
            String accessToken = "access-token";
            Token token = Token.builder()
                    .id("refresh-token-id")
                    .refreshToken(refreshToken)
                    .expireTime(refreshTokenExpiration)
                    .isValid(false)
                    .user(UserMother.user().build())
                    .build();

            Jwt jwt = Jwt.withTokenValue(refreshToken)
                    .expiresAt(refreshTokenExpiration)
                    .subject(token.getUser().getEmail())
                    .claim("id", token.getId())
                    .header("alg", "SH252")
                    .build();

            given(tokenService.validateJWT(refreshToken)).willReturn(jwt);
            given(tokenRepository.findById(token.getId())).willReturn(Optional.of(token));
            given(clock.instant()).willReturn(currentTime);

            // When
            assertThatThrownBy(() -> cut.refreshAccessToken(refreshToken))
                    .isInstanceOf(RefreshTokenException.class)
                    .hasMessage(ErrorMessages.INVALIDATED_REFRESH_TOKEN);

            // Then
            then(tokenService).shouldHaveNoMoreInteractions();
            then(userMapper).shouldHaveNoInteractions();
        }

        @Test
        void refreshAccessToken_WithCookieAndDatabaseTokenNotMatching_ShouldThrowRefreshTokenException() {
            // Given
            LocalDateTime defaultLocalDateTime = LocalDateTime.of(2024, 12, 13, 12, 15);
            Clock fixedClock = Clock.fixed(defaultLocalDateTime.toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
            Instant currentTime = fixedClock.instant();
            Instant refreshTokenExpiration = fixedClock.instant().plus(7, ChronoUnit.DAYS);

            String refreshToken = "refresh-token";
            Token token = Token.builder()
                    .id("refresh-token-id")
                    .refreshToken("different-refresh-token")
                    .expireTime(refreshTokenExpiration)
                    .isValid(true)
                    .user(UserMother.user().build())
                    .build();

            Jwt jwt = Jwt.withTokenValue(refreshToken)
                    .expiresAt(refreshTokenExpiration)
                    .subject(token.getUser().getEmail())
                    .claim("id", token.getId())
                    .header("alg", "SH252")
                    .build();

            given(tokenService.validateJWT(refreshToken)).willReturn(jwt);
            given(tokenRepository.findById(token.getId())).willReturn(Optional.of(token));
            given(clock.instant()).willReturn(currentTime);

            // When
            assertThatThrownBy(() -> cut.refreshAccessToken(refreshToken))
                    .isInstanceOf(RefreshTokenException.class)
                    .hasMessage(ErrorMessages.COOKIE_REFRESH_TOKEN_AND_DB_TOKEN_MISMATCH);

            // Then
            then(tokenService).shouldHaveNoMoreInteractions();
            then(userMapper).shouldHaveNoInteractions();
        }

        @Test
        void refreshAccessToken_WithExpiredToken_ShouldThrowRefreshAccessTokenException() {
            // Given
            LocalDateTime defaultLocalDateTime = LocalDateTime.of(2024, 12, 13, 12, 15);
            Clock fixedClock = Clock.fixed(defaultLocalDateTime.toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
            Instant currentTime = fixedClock.instant();
            Instant refreshTokenExpiration = fixedClock.instant().plus(7, ChronoUnit.DAYS);

            String refreshToken = "refresh-token";
            Token token = Token.builder()
                    .id("refresh-token-id")
                    .refreshToken(refreshToken)
                    .expireTime(currentTime.minusSeconds(100))
                    .isValid(true)
                    .user(UserMother.user().build())
                    .build();

            Jwt jwt = Jwt.withTokenValue(refreshToken)
                    .expiresAt(refreshTokenExpiration)
                    .subject(token.getUser().getEmail())
                    .claim("id", token.getId())
                    .header("alg", "SH252")
                    .build();

            given(tokenService.validateJWT(refreshToken)).willReturn(jwt);
            given(tokenRepository.findById(token.getId())).willReturn(Optional.of(token));
            given(clock.instant()).willReturn(currentTime);

            // When
            assertThatThrownBy(() -> cut.refreshAccessToken(refreshToken))
                    .isInstanceOf(RefreshTokenException.class)
                    .hasMessage(ErrorMessages.EXPIRED_REFRESH_TOKEN);

            // Then
            then(tokenService).shouldHaveNoMoreInteractions();
            then(userMapper).shouldHaveNoInteractions();

        }
    }

}
