package com.selflearntech.techblogbackend.user;

import com.selflearntech.techblogbackend.user.dto.UserAuthenticationRequestDTO;
import com.selflearntech.techblogbackend.user.dto.UserAuthenticationResponseDTO;
import com.selflearntech.techblogbackend.user.dto.UserDTO;
import com.selflearntech.techblogbackend.user.dto.UserRegistrationRequestDTO;
import com.selflearntech.techblogbackend.user.model.User;

import java.util.Set;

public class UserMother {

    public static UserRegistrationRequestDTO.UserRegistrationRequestDTOBuilder userRegistrationPayload() {
        return UserRegistrationRequestDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .password("C11l08a#05")
                .verifyPassword("C11l08a#05");
    }

    public static UserAuthenticationRequestDTO.UserAuthenticationRequestDTOBuilder userAuthenticationPayload() {
        return UserAuthenticationRequestDTO.builder()
                .email("john.doe@gmail.com")
                .password("C11l08a#05");
    }

    public static UserAuthenticationResponseDTO.UserAuthenticationResponseDTOBuilder userAuthenticationResponsePayload() {
        return UserAuthenticationResponseDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .roles(Set.of("USER"))
                .profileImg("")
                .accessToken("access-token")
                .refreshToken("refresh-token");
    }

    public static UserDTO.UserDTOBuilder userDTO() {
        return UserDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .profileImg("")
                .email("john.doe@gmail.com")
                .accessToken("access-token")
                .roles(Set.of("USER"));
    }

    public static User.UserBuilder user() {
        return User.builder()
                .id(1)
                .firstName("John")
                .lastName("Last")
                .profileImg("")
                .email("john.doe@gmail.com");
    }
}
