package com.selflearntech.techblogbackend.user;

import com.selflearntech.techblogbackend.user.dto.AuthenticationRequestDTO;
import com.selflearntech.techblogbackend.user.dto.AuthenticationResponseDTO;
import com.selflearntech.techblogbackend.user.dto.UserDTO;
import com.selflearntech.techblogbackend.user.dto.RegistrationRequestDTO;
import com.selflearntech.techblogbackend.user.model.Role;
import com.selflearntech.techblogbackend.user.model.RoleType;
import com.selflearntech.techblogbackend.user.model.User;

import java.util.Set;

public class UserMother {

    public static RegistrationRequestDTO.RegistrationRequestDTOBuilder registrationPayload() {
        return RegistrationRequestDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .password("C11l08a#05")
                .verifyPassword("C11l08a#05");
    }

    public static AuthenticationRequestDTO.AuthenticationRequestDTOBuilder authenticationPayload() {
        return AuthenticationRequestDTO.builder()
                .email("john.doe@gmail.com")
                .password("C11l08a#05");
    }

    public static AuthenticationResponseDTO.AuthenticationResponseDTOBuilder authenticationResponsePayload() {
        return AuthenticationResponseDTO.builder()
                .id("67983098a363fb3d06132a51")
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
                .id("64f57b8a18c4a1a5f9f8c671")
                .firstName("John")
                .lastName("Last")
                .authorities(Set.of(new Role("role-id", RoleType.USER)))
                .profileImg("")
                .email("john.doe@gmail.com");
    }
}
