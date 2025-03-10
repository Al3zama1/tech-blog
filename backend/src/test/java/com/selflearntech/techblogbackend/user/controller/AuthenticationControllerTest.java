package com.selflearntech.techblogbackend.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selflearntech.techblogbackend.config.SecurityConfig;
import com.selflearntech.techblogbackend.exception.*;
import com.selflearntech.techblogbackend.user.UserMother;
import com.selflearntech.techblogbackend.user.dto.UserAuthenticationRequestDTO;
import com.selflearntech.techblogbackend.user.dto.UserAuthenticationResponseDTO;
import com.selflearntech.techblogbackend.user.dto.UserDTO;
import com.selflearntech.techblogbackend.user.dto.UserRegistrationRequestDTO;
import com.selflearntech.techblogbackend.user.mapper.UserMapper;
import com.selflearntech.techblogbackend.user.model.RoleType;
import com.selflearntech.techblogbackend.user.service.IAuthenticationService;
import com.selflearntech.techblogbackend.token.util.RSAKeyProperties;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.selflearntech.techblogbackend.utils.ResponseBodyMatchers.responseBody;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
@Import({SecurityConfig.class, RSAKeyProperties.class})
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private IAuthenticationService authenticationService;
    @MockitoBean
    private UserDetailsService userDetailsService;
    @MockitoBean
    private UserMapper userMapper;

    @Nested
    public class UserRegistration {

        @Test
        void registerUser_WithValidData_ShouldReturn201Status() throws Exception {
            // Given
            UserRegistrationRequestDTO registrationPayload = UserMother.registrationPayload().build();

            given(authenticationService.registerUser(registrationPayload)).willReturn("userId");

            // When
            mockMvc.perform(post("/api/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registrationPayload)))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "/api/v1/users/userId"));


            // Then
            then(authenticationService).should().registerUser(registrationPayload);
        }

        @Test
        void registerUser_WithValidDataButFailToAssignUserRole_ShouldReturn500Status() throws Exception {
            // Given
            UserRegistrationRequestDTO registrationPayload = UserMother.registrationPayload().build();

            doThrow(new RoleAssignmentException(ErrorMessages.ROLE_ASSIGNMENT_FAILURE, RoleType.USER.name()))
                    .when(authenticationService).registerUser(registrationPayload);

            // When
            mockMvc.perform(post("/api/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registrationPayload)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(responseBody().containsErrorMessage(ErrorMessages.UNKNOWN_ERROR));

            // Then
            then(authenticationService).should().registerUser(registrationPayload);
        }

        @Test
        void registerUser_WithInvalidEmailFormat_ShouldReturn400StatusWithInputValidationError() throws Exception {
            // Given
            UserRegistrationRequestDTO registrationPayload = UserMother.registrationPayload()
                    .email("john.doe.com")
                    .build();

            // When
            mockMvc.perform(post("/api/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registrationPayload)))
                    .andExpect(status().isBadRequest())
                    .andExpect(responseBody().containsValidationError("email", "must be a well-formed email address"));

            // Then
            then(authenticationService).shouldHaveNoInteractions();
        }

        @Test
        void registerUser_WithExistingEmail_ShouldReturn409StatusWithErrorMessage() throws Exception {
            // Given
            UserRegistrationRequestDTO registrationPayload = UserMother.registrationPayload().build();

            doThrow(new ConflictException(ErrorMessages.EMAIL_TAKEN)).when(authenticationService).registerUser(registrationPayload);

            // When, Then
            mockMvc.perform(post("/api/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registrationPayload)))
                    .andExpect(status().isConflict())
                    .andExpect(responseBody().containsErrorMessage(ErrorMessages.EMAIL_TAKEN));
        }

        @Test
        void registerUser_WithNonMatchingPasswords_ShouldReturn400StatusWithErrorMessage() throws Exception {
            // Given
            UserRegistrationRequestDTO registrationPayload = UserMother.registrationPayload()
                    .password("C11l08a#0522")
                    .verifyPassword("C11l08a#0523")
                    .build();

            doThrow(new BadRequestException(ErrorMessages.PASSWORDS_MUST_MATCH))
                    .when(authenticationService).registerUser(registrationPayload);

            // When, Then
            mockMvc.perform(post("/api/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registrationPayload)))
                    .andExpect(status().isBadRequest())
                    .andExpect(responseBody().containsErrorMessage(ErrorMessages.PASSWORDS_MUST_MATCH));
        }
    }

    @Nested
    public class UserAuthentication {

        @Test
        void authenticateUser_WithValidData_ShouldReturn200StatusWithUserDTO() throws Exception {
            // Given
            UserAuthenticationRequestDTO authenticationPayload = UserMother.userAuthenticationPayload().build();
            UserAuthenticationResponseDTO authenticationResponse = UserMother.authenticationResponsePayload().build();

            given(authenticationService.authenticateUser(authenticationPayload.getEmail(), authenticationPayload.getPassword()))
                    .willReturn(authenticationResponse);

            // When
            mockMvc.perform(post("/api/v1/auth/authenticate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authenticationPayload)))
                    .andExpect(status().isOk())
                    .andExpect(cookie().value("refresh-token", authenticationResponse.getRefreshToken()))
                    .andExpect(cookie().httpOnly("refresh-token", true))
//                    .andExpect(cookie().domain("refresh-token", "localhost"))
                    .andExpect(cookie().path("refresh-token", "/api/v1/auth/refresh-access"));

            // Then
            then(authenticationService).should().authenticateUser(authenticationPayload.getEmail(), authenticationPayload.getPassword());
            then(userMapper).should().toUserDTO(authenticationResponse);
        }

        @Test
        void authenticateUser_WithIncorrectEmail_ShouldReturn401StatusWithErrorMessage() throws Exception {
            // Given
            UserAuthenticationRequestDTO authenticationPayload = UserMother.userAuthenticationPayload().build();

            given(authenticationService.authenticateUser(authenticationPayload.getEmail(), authenticationPayload.getPassword()))
                    .willThrow(new UsernameNotFoundException(ErrorMessages.INVALID_CREDENTIALS));

            // When, Then
            mockMvc.perform(post("/api/v1/auth/authenticate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authenticationPayload)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(responseBody().containsErrorMessage(ErrorMessages.INVALID_CREDENTIALS));
        }

        @Test
        void authenticateUser_WithIncorrectPassword_ShouldReturn401StatusWithErrorMessage() throws Exception {
            // Given
            UserAuthenticationRequestDTO authenticationPayload = UserMother.userAuthenticationPayload().build();

            given(authenticationService.authenticateUser(authenticationPayload.getEmail(), authenticationPayload.getPassword()))
                    .willThrow(new BadCredentialsException(ErrorMessages.INVALID_CREDENTIALS));

            // When, Then
            mockMvc.perform(post("/api/v1/auth/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authenticationPayload)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(responseBody().containsErrorMessage(ErrorMessages.INVALID_CREDENTIALS));
        }

        @Test
        void authenticateUserWithInvalidEmailFormat_ShouldReturn400StatusWithValidationError() throws Exception {
            // Given
            UserAuthenticationRequestDTO authenticationPayload = UserMother.userAuthenticationPayload()
                    .email("john.doe.com")
                    .build();

            // When
            mockMvc.perform(post("/api/v1/auth/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authenticationPayload)))
                    .andExpect(status().isBadRequest())
                    .andExpect(responseBody().containsValidationError("email", "must be a well-formed email address"));

            // Then
            then(authenticationService).shouldHaveNoInteractions();
        }
    }

    @Nested
    public class RefreshToken {
        @Test
        void refreshAccessToken_WithValidCookie_ShouldReturn200StatusWithNewAccessToken() throws Exception {
            // Given
            String refreshToken = "refresh-token";
            MockCookie refreshTokenCookie = new MockCookie("refresh-token", refreshToken);
            UserDTO responseUserDTO = UserMother.userDTO().build();

            given(authenticationService.refreshAccessToken(refreshToken)).willReturn(responseUserDTO);

            // When
            mockMvc.perform(post("/api/v1/auth/refresh-access")
                    .contentType(MediaType.APPLICATION_JSON)
                    .cookie(refreshTokenCookie))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(responseBody().containsObjectAsJson(responseUserDTO, UserDTO.class));

            // Then
            then(authenticationService).should().refreshAccessToken(refreshToken);
        }

        @Test
        void refreshAccessToken_WithMissingRefreshTokenCookie_ShouldReturn400Status() throws Exception {
            // Given

            // When
            mockMvc.perform(post("/api/v1/auth/refresh-access")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            // Then
            then(authenticationService).shouldHaveNoInteractions();
        }

        @Test
        void refreshAccessToken_WithInvalidRefreshToken_ShouldReturn401Status() throws Exception {
            // Given
            String refreshToken = "refresh-token";
            MockCookie refreshTokenCookie = new MockCookie("refresh-token", refreshToken);

            given(authenticationService.refreshAccessToken(refreshToken)).willThrow(new RefreshTokenException(ErrorMessages.INVALID_REFRESH_TOKEN + ": " + ErrorMessages.FAILED_TOKEN_DECODE));

            // When
            mockMvc.perform(post("/api/v1/auth/refresh-access")
                    .contentType(MediaType.APPLICATION_JSON)
                    .cookie(refreshTokenCookie))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(header().string("Set-Cookie", "refresh-token="))
                    .andExpect(responseBody().containsErrorMessage(ErrorMessages.INVALID_REFRESH_TOKEN));

            // Then
            then(authenticationService).should().refreshAccessToken(refreshToken);
        }
    }

}
