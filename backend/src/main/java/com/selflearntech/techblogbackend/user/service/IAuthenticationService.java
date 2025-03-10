package com.selflearntech.techblogbackend.user.service;

import com.selflearntech.techblogbackend.user.dto.UserAuthenticationResponseDTO;
import com.selflearntech.techblogbackend.user.dto.UserDTO;
import com.selflearntech.techblogbackend.user.dto.UserRegistrationRequestDTO;

public interface IAuthenticationService {

    String registerUser(UserRegistrationRequestDTO registerDTO);

    UserAuthenticationResponseDTO authenticateUser(String email, String password);

    UserDTO refreshAccessToken(String refreshToken);
}
