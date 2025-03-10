package com.selflearntech.techblogbackend.user.mapper;

import com.selflearntech.techblogbackend.user.UserMother;
import com.selflearntech.techblogbackend.user.dto.AuthenticationResponseDTO;
import com.selflearntech.techblogbackend.user.dto.UserDTO;
import com.selflearntech.techblogbackend.user.model.Role;
import com.selflearntech.techblogbackend.user.model.RoleType;
import com.selflearntech.techblogbackend.user.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private static UserMapper cut;

    @BeforeAll
    static void beforeAll() {
        cut = Mappers.getMapper(UserMapper.class);
    }

    @Test
    void toUserDTOFromAuthenticationResponseDTO() {
        // Given
        AuthenticationResponseDTO authenticationResponse = UserMother.authenticationResponsePayload().build();

        // When
        UserDTO userDTO = cut.toUserDTO(authenticationResponse);

        // Then
        assertThat(userDTO.getId()).isEqualTo(authenticationResponse.getId());
        assertThat(userDTO.getFirstName()).isEqualTo(authenticationResponse.getFirstName());
        assertThat(userDTO.getLastName()).isEqualTo(authenticationResponse.getLastName());
        assertThat(userDTO.getEmail()).isEqualTo(authenticationResponse.getEmail());
        assertThat(userDTO.getProfileImg()).isEqualTo(authenticationResponse.getProfileImg());
        assertThat(userDTO.getRoles()).isEqualTo(authenticationResponse.getRoles());
        assertThat(userDTO.getAccessToken()).isEqualTo(authenticationResponse.getAccessToken());
    }

    @Test
    void toAuthenticationResponseDTOFromUserAndRefreshTokenAndAccessToken() {
        // Given
        User user = UserMother.user().build();
        String accessToken = "access-token";
        String refreshToken = "refresh-token";

        // When
        AuthenticationResponseDTO authenticationResponse = cut.toAuthenticationResponseDTO(user, refreshToken, accessToken);
        Set<String> userRoles = cut.getRoles(user);

        // Then
        assertThat(authenticationResponse.getId()).isEqualTo(user.getId());
        assertThat(authenticationResponse.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(authenticationResponse.getLastName()).isEqualTo(user.getLastName());
        assertThat(authenticationResponse.getEmail()).isEqualTo(user.getEmail());
        assertThat(authenticationResponse.getProfileImg()).isEqualTo(user.getProfileImg());
        assertThat(authenticationResponse.getRoles()).isEqualTo(userRoles);
        assertThat(authenticationResponse.getRefreshToken()).isEqualTo(refreshToken);
        assertThat(authenticationResponse.getAccessToken()).isEqualTo(accessToken);
    }

    @Test
    void toUserDTOFromUserAndAccessToken() {
        // Given
        User user = UserMother.user().build();
        String accessToken = "access-token";

        // When
        UserDTO userDTO = cut.toUserDTO(user, accessToken);

        // Then
        assertThat(userDTO.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(userDTO.getLastName()).isEqualTo(user.getLastName());
        assertThat(userDTO.getEmail()).isEqualTo(user.getEmail());
        assertThat(userDTO.getProfileImg()).isEqualTo(user.getProfileImg());
        assertThat(userDTO.getAccessToken()).isEqualTo(accessToken);
        assertThat(userDTO.getRoles()).contains("USER");
    }

    @Test
    void getRolesFromUserAuthorities() {
        // Given
        Role userRole = new Role("role-1", RoleType.USER);
        Role adminRole = new Role("role-2", RoleType.ADMIN);
        User user = UserMother.user()
                .authorities(Set.of(userRole, adminRole))
                .build();

        // When
        Set<String> userRoles = cut.getRoles(user);

        // Then
        assertThat(userRoles.size()).isEqualTo(2);
        assertThat(userRoles).contains("ADMIN");
        assertThat(userRoles).contains("USER");
    }

}
