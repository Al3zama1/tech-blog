package com.selflearntech.techblogbackend.user.mapper;

import com.selflearntech.techblogbackend.user.dto.AuthenticationResponseDTO;
import com.selflearntech.techblogbackend.user.dto.UserDTO;
import com.selflearntech.techblogbackend.user.model.Role;
import com.selflearntech.techblogbackend.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toUserDTO(AuthenticationResponseDTO authenticationResponseDTO);

    @Mapping(expression = "java(getRoles(user))", target = "roles")
    AuthenticationResponseDTO toAuthenticationResponseDTO(User user, String refreshToken, String accessToken);

    @Mapping(expression = "java(getRoles(user))", target = "roles")
    UserDTO toUserDTO(User user, String accessToken);

    default Set<String> getRoles(User user) {
        return user.getAuthorities().stream().map(Role::getAuthority).collect(Collectors.toSet());
    }
}
