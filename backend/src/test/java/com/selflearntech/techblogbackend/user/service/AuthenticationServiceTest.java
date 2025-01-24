package com.selflearntech.techblogbackend.user.service;

import com.selflearntech.techblogbackend.exception.*;
import com.selflearntech.techblogbackend.token.model.Token;
import com.selflearntech.techblogbackend.token.service.TokenService;
import com.selflearntech.techblogbackend.user.UserMother;
import com.selflearntech.techblogbackend.user.dto.UserAuthenticationRequestDTO;
import com.selflearntech.techblogbackend.user.dto.UserRegistrationRequestDTO;
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
import org.springframework.security.authentication.AbstractAuthenticationToken;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private Clock clock;
    @Mock
    private UserRepository userRepository;
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
            UserRegistrationRequestDTO registrationPayload = UserMother.userRegistrationPayload().build();
            Role userRole = Role.builder().authority(RoleType.USER).build();
            String encodedPassword = "encoded-password";

            given(userRepository.existsUserByEmail(registrationPayload.getEmail())).willReturn(false);
            given(roleRepository.findByAuthority(RoleType.USER)).willReturn(Optional.of(userRole));
            given(passwordEncoder.encode(registrationPayload.getPassword())).willReturn(encodedPassword);

            // When
            cut.registerUser(registrationPayload);

            // Then
            ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
            then(userRepository).should().save(userArgumentCaptor.capture());

            User savedUser = userArgumentCaptor.getValue();
            assertThat(savedUser.getPassword()).isEqualTo(encodedPassword);
            assertThat(savedUser.getAuthorities().size()).isEqualTo(1);
            assertThat(savedUser.getAuthorities().stream().findFirst().get().getAuthority()).isEqualTo(RoleType.USER.name());
        }

        @Test
        void registerUser_WithNonMatchingPasswords_ShouldThrowBadRequestException() {
            // Given
            UserRegistrationRequestDTO registrationPayload = UserMother.userRegistrationPayload()
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
        void registerUser_WithEmailAlreadyTaken_ShouldThrowUserExistsException() {
            // Given
            UserRegistrationRequestDTO registrationPayload = UserMother.userRegistrationPayload().build();

            given(userRepository.existsUserByEmail(registrationPayload.getEmail())).willReturn(true);

            // When
            assertThatThrownBy(() -> cut.registerUser(registrationPayload))
                    .isInstanceOf(UserExistsException.class)
                            .hasMessage(ErrorMessages.EMAIL_TAKEN);

            // Then
            then(userRepository).should(never()).save(any(User.class));
            then(roleRepository).shouldHaveNoInteractions();
            then(passwordEncoder).shouldHaveNoInteractions();
        }

        @Test
        void registerUser_FailToAssignUserRole_ShouldThrowRoleAssignmentException() {
            // Given
            UserRegistrationRequestDTO registrationPayload = UserMother.userRegistrationPayload().build();

            given(userRepository.existsUserByEmail(registrationPayload.getEmail())).willReturn(false);
            given(roleRepository.findByAuthority(RoleType.USER)).willReturn(Optional.empty());

            // When
            assertThatThrownBy(() -> cut.registerUser(registrationPayload))
                    .isInstanceOf(RoleAssignmentException.class)
                    .hasMessage(ErrorMessages.ROLE_ASSIGNMENT_FAILURE + ": " + RoleType.USER.name());

            // Then
            then(userRepository).should(never()).save(any(User.class));

        }
    }

    @Nested
    class UserAuthentication {
        @Test
        void authenticateUser_WithValidCredentials_ShouldReturnAuthenticationResponseDTO() {
            // Given
            UserAuthenticationRequestDTO authenticationPayload = UserMother.userAuthenticationPayload().build();
            User authenticatedUser = UserMother.user().build();
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authenticatedUser, null);

            String accessToken = "access-token";
            String refreshToken = "refresh-token";

            LocalDateTime defaultLocalDateTime = LocalDateTime.of(2024, 12, 13, 12, 15);
            Clock fixedClock = Clock.fixed(defaultLocalDateTime.toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
            Instant currentTime = fixedClock.instant();
            Instant refreshTokenExpiration = fixedClock.instant().plus(7, ChronoUnit.DAYS);


            given(authenticationManager.authenticate(any(AbstractAuthenticationToken.class))).willReturn(authenticationToken);
            given(clock.instant()).willReturn(currentTime);
            given(tokenService.createRefreshToken(authenticatedUser.getEmail(), refreshTokenExpiration)).willReturn(refreshToken);
            given(tokenService.createAccessToken(authenticatedUser)).willReturn(accessToken);

            // When
            cut.authenticateUser(authenticationPayload.getEmail(), authenticationPayload.getPassword());

            // Then
            ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
            then(userRepository).should().save(userArgumentCaptor.capture());

            authenticatedUser = userArgumentCaptor.getValue();
            assertThat(authenticatedUser).isNotNull();
            assertThat(authenticatedUser.getToken().getRefreshToken()).isEqualTo(refreshToken);
            assertThat(authenticatedUser.getToken().isValid()).isTrue();
            assertThat(authenticatedUser.getToken().getExpireTime()).isEqualTo(refreshTokenExpiration);

            then(userMapper).should().toUserAuthenticationResponseDTO(any(), eq(accessToken));
        }

        @Test
        void authenticateUser_OnSubsequentLoginReuseTokenWithDifferentValue_ShouldReturnAuthenticationResponseDTO() {
            // Given
            UserAuthenticationRequestDTO authenticationPayload = UserMother.userAuthenticationPayload().build();
            User authenticatedUser = UserMother.user().token(new Token()).build();
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authenticatedUser, null);

            String accessToken = "access-token";
            String refreshToken = "refresh-token";

            LocalDateTime defaultLocalDateTime = LocalDateTime.of(2024, 12, 13, 12, 15);
            Clock fixedClock = Clock.fixed(defaultLocalDateTime.toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
            Instant currentTime = fixedClock.instant();
            Instant refreshTokenExpiration = fixedClock.instant().plus(7, ChronoUnit.DAYS);


            given(authenticationManager.authenticate(any(AbstractAuthenticationToken.class))).willReturn(authenticationToken);
            given(clock.instant()).willReturn(currentTime);
            given(tokenService.createRefreshToken(authenticatedUser.getEmail(), refreshTokenExpiration)).willReturn(refreshToken);
            given(tokenService.createAccessToken(authenticatedUser)).willReturn(accessToken);

            // When
            cut.authenticateUser(authenticationPayload.getEmail(), authenticationPayload.getPassword());

            // Then
            ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
            then(userRepository).should().save(userArgumentCaptor.capture());

            authenticatedUser = userArgumentCaptor.getValue();
            assertThat(authenticatedUser).isNotNull();
            assertThat(authenticatedUser.getToken().getRefreshToken()).isEqualTo(refreshToken);
            assertThat(authenticatedUser.getToken().isValid()).isTrue();
            assertThat(authenticatedUser.getToken().getExpireTime()).isEqualTo(refreshTokenExpiration);

            then(userMapper).should().toUserAuthenticationResponseDTO(any(), eq(accessToken));
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
                    .refreshToken(refreshToken)
                    .expireTime(refreshTokenExpiration)
                    .isValid(true)
                    .build();
            User user = UserMother.user().token(token).build();
            Jwt jwt = Jwt.withTokenValue(refreshToken)
                    .expiresAt(refreshTokenExpiration)
                    .subject(user.getEmail())
                    .header("alg", "SH252")
                    .build();

            given(tokenService.validateJWT(refreshToken)).willReturn(jwt);
            given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
            given(clock.instant()).willReturn(currentTime);
            given(tokenService.createAccessToken(user)).willReturn(accessToken);

            // When
            cut.refreshAccessToken(refreshToken);

            // Then
            then(tokenService).should().createAccessToken(user);
            then(userMapper).should().toUserDTO(user, accessToken);
        }

        @Test
        void refreshAccessToken_WithTokenValidationException_ShouldThrowRefreshAccessTokenException() {
            // Given
            String refreshToken = "refresh-token";

            given(tokenService.validateJWT(refreshToken)).willThrow(new JwtException(ErrorMessages.INVALID_REFRESH_TOKEN));

            // When
            assertThatThrownBy(() -> cut.refreshAccessToken(refreshToken))
                    .isInstanceOf(RefreshTokenException.class)
                    .hasMessage(ErrorMessages.INVALID_REFRESH_TOKEN + ": " + ErrorMessages.FAILED_TOKEN_DECODE);

            // Then
            then(userRepository).shouldHaveNoInteractions();
            then(tokenService).shouldHaveNoMoreInteractions();
            then(userMapper).shouldHaveNoInteractions();
        }

        @Test
        void refreshAccessToken_WithNonExistingSubject_ShouldThrowRefreshAccessTokenException() {
            // Given
            String refreshToken = "refresh-token";
            String subject = "john.doe@gmail.com";
            Jwt jwt = Jwt.withTokenValue(refreshToken)
                    .subject(subject)
                    .header("alg", "SH252")
                    .build();

            given(tokenService.validateJWT(refreshToken)).willReturn(jwt);
            given(userRepository.findByEmail(subject)).willReturn(Optional.empty());

            // When
            assertThatThrownBy(() -> cut.refreshAccessToken(refreshToken))
                    .isInstanceOf(RefreshTokenException.class)
                    .hasMessage(ErrorMessages.TOKEN_SUBJECT_DOES_NOT_LINK_TO_USER);

            // Then
            then(tokenService).shouldHaveNoMoreInteractions();
            then(userMapper).shouldHaveNoInteractions();
        }

        @Test
        void refreshAccessToken_WithInvalidatedToken_ShouldThrowRefreshAccessTokenException() {
            // Given
            LocalDateTime defaultLocalDateTime = LocalDateTime.of(2024, 12, 13, 12, 15);
            Clock fixedClock = Clock.fixed(defaultLocalDateTime.toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
            Instant currentTime = fixedClock.instant();
            Instant refreshTokenExpiration = fixedClock.instant().plus(7, ChronoUnit.DAYS);

            String refreshToken = "refresh-token";
            Token token = Token.builder()
                    .refreshToken(refreshToken)
                    .expireTime(refreshTokenExpiration)
                    .isValid(false)
                    .build();
            User user = UserMother.user().token(token).build();
            Jwt jwt = Jwt.withTokenValue(refreshToken)
                    .expiresAt(refreshTokenExpiration)
                    .subject(user.getEmail())
                    .header("alg", "SH252")
                    .build();

            given(tokenService.validateJWT(refreshToken)).willReturn(jwt);
            given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
            given(clock.instant()).willReturn(currentTime);

            // When
            assertThatThrownBy(() -> cut.refreshAccessToken(refreshToken))
                    .isInstanceOf(RefreshTokenException.class)
                    .hasMessage(ErrorMessages.INVALID_REFRESH_TOKEN + ": " + ErrorMessages.INVALIDATED_REFRESH_TOKEN);

            // Then
            then(tokenService).should(never()).createAccessToken(user);
            then(userMapper).shouldHaveNoInteractions();
        }

        @Test
        void refreshAccessToken_WithCookieAndDatabaseTokenNotMatching_ShouldThrowRefreshAccessTokenException() {
            // Given
            LocalDateTime defaultLocalDateTime = LocalDateTime.of(2024, 12, 13, 12, 15);
            Clock fixedClock = Clock.fixed(defaultLocalDateTime.toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
            Instant currentTime = fixedClock.instant();
            Instant refreshTokenExpiration = fixedClock.instant().plus(7, ChronoUnit.DAYS);

            String refreshToken = "refresh-token";
            Token token = Token.builder()
                    .refreshToken("different-refresh-token")
                    .expireTime(refreshTokenExpiration)
                    .isValid(true)
                    .build();
            User user = UserMother.user().token(token).build();
            Jwt jwt = Jwt.withTokenValue(refreshToken)
                    .expiresAt(refreshTokenExpiration)
                    .subject(user.getEmail())
                    .header("alg", "SH252")
                    .build();

            given(tokenService.validateJWT(refreshToken)).willReturn(jwt);
            given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
            given(clock.instant()).willReturn(currentTime);

            // When
            assertThatThrownBy(() -> cut.refreshAccessToken(refreshToken))
                    .isInstanceOf(RefreshTokenException.class)
                    .hasMessage(ErrorMessages.INVALID_REFRESH_TOKEN + ": " + ErrorMessages.COOKIE_REFRESH_TOKEN_AND_DB_TOKEN_MISMATCH);

            // Then
            then(tokenService).should(never()).createAccessToken(user);
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
                    .refreshToken(refreshToken)
                    .expireTime(currentTime.minusSeconds(100))
                    .isValid(true)
                    .build();
            User user = UserMother.user().token(token).build();
            Jwt jwt = Jwt.withTokenValue(refreshToken)
                    .expiresAt(refreshTokenExpiration)
                    .subject(user.getEmail())
                    .header("alg", "SH252")
                    .build();

            given(tokenService.validateJWT(refreshToken)).willReturn(jwt);
            given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
            given(clock.instant()).willReturn(currentTime);

            // When
            assertThatThrownBy(() -> cut.refreshAccessToken(refreshToken))
                    .isInstanceOf(RefreshTokenException.class)
                    .hasMessage(ErrorMessages.INVALID_REFRESH_TOKEN + ": " + ErrorMessages.EXPIRED_REFRESH_TOKEN);

            // Then
            then(tokenService).should(never()).createAccessToken(user);
            then(userMapper).shouldHaveNoInteractions();
        }

    }

}
