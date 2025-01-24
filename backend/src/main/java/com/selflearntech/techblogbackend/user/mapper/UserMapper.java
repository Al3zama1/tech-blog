package com.selflearntech.techblogbackend.user.mapper;

import com.selflearntech.techblogbackend.user.dto.UserAuthenticationResponseDTO;
import com.selflearntech.techblogbackend.user.dto.UserDTO;
import com.selflearntech.techblogbackend.user.model.Role;
import com.selflearntech.techblogbackend.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toUserDTO(UserAuthenticationResponseDTO userAuthenticationResponseDTO);

    @Mapping(expression = "java(user.getToken().getRefreshToken())", target = "refreshToken")
    @Mapping(expression = "java(getRoles(user))", target = "roles")
    UserAuthenticationResponseDTO toUserAuthenticationResponseDTO(User user, String accessToken);

    @Mapping(expression = "java(getRoles(user))", target = "roles")
    UserDTO toUserDTO(User user, String accessToken);

    default Set<String> getRoles(User user) {
        return user.getAuthorities().stream().map(Role::getAuthority).collect(Collectors.toSet());
    }
}
