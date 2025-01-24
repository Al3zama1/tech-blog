package com.selflearntech.techblogbackend.user.mapper;

import com.selflearntech.techblogbackend.token.model.Token;
import com.selflearntech.techblogbackend.user.UserMother;
import com.selflearntech.techblogbackend.user.dto.UserAuthenticationResponseDTO;
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
    void toUserDTOFromUserAuthenticationResponseDTO() {
        // Given
        UserAuthenticationResponseDTO authenticationResponse = UserMother.userAuthenticationResponsePayload().build();

        // When
        UserDTO userDTO = cut.toUserDTO(authenticationResponse);

        // Then
        assertThat(userDTO.getFirstName()).isEqualTo(authenticationResponse.getFirstName());
        assertThat(userDTO.getLastName()).isEqualTo(authenticationResponse.getLastName());
        assertThat(userDTO.getEmail()).isEqualTo(authenticationResponse.getEmail());
        assertThat(userDTO.getProfileImg()).isEqualTo(authenticationResponse.getProfileImg());
        assertThat(userDTO.getRoles()).isEqualTo(authenticationResponse.getRoles());
        assertThat(userDTO.getAccessToken()).isEqualTo(authenticationResponse.getAccessToken());
    }

    @Test
    void toUserAuthenticationResponseDTOFromUserAndAccessToken() {
        // Given
        Token refreshToken = Token.builder().refreshToken("refresh-token").build();
        Role userRole = new Role(1, RoleType.USER);
        User user = UserMother.user().token(refreshToken).authorities(Set.of(userRole)).build();
        String accessToken = "access-token";

        // When
        UserAuthenticationResponseDTO authenticationResponse = cut.toUserAuthenticationResponseDTO(user, accessToken);
        Set<String> userRoles = cut.getRoles(user);

        // Then
        assertThat(authenticationResponse.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(authenticationResponse.getLastName()).isEqualTo(user.getLastName());
        assertThat(authenticationResponse.getEmail()).isEqualTo(user.getEmail());
        assertThat(authenticationResponse.getProfileImg()).isEqualTo(user.getProfileImg());
        assertThat(authenticationResponse.getRoles()).isEqualTo(userRoles);
        assertThat(authenticationResponse.getRefreshToken()).isEqualTo(user.getToken().getRefreshToken());
        assertThat(authenticationResponse.getAccessToken()).isEqualTo(accessToken);
    }

    @Test
    void toUserDTOFromUserAndAccessToken() {
        // Given
        Role userRole = new Role(1, RoleType.USER);
        User user = UserMother.user().authorities(Set.of(userRole)).build();
        String accessToken = "access-token";

        // When
        UserDTO userDTO = cut.toUserDTO(user, accessToken);
        Set<String> userRoles = cut.getRoles(user);

        // Then
        assertThat(userDTO.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(userDTO.getLastName()).isEqualTo(user.getLastName());
        assertThat(userDTO.getEmail()).isEqualTo(user.getEmail());
        assertThat(userDTO.getProfileImg()).isEqualTo(user.getProfileImg());
        assertThat(userDTO.getRoles()).isEqualTo(userRoles);
        assertThat(userDTO.getAccessToken()).isEqualTo(accessToken);
    }

    @Test
    void getRolesFromUserAuthorities() {
        // Given
        Role userRole = new Role(1, RoleType.USER);
        Role adminRole = new Role(2, RoleType.ADMIN);
        User user = UserMother.user().authorities(Set.of(userRole, adminRole)).build();

        // When
        Set<String> userRoles = cut.getRoles(user);

        // Then
        assertThat(userRoles.size()).isEqualTo(2);
        assertThat(userRoles).contains("ADMIN");
        assertThat(userRoles).contains("USER");
    }

}
