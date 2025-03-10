package com.selflearntech.techblogbackend.user.service;

import com.selflearntech.techblogbackend.user.dto.AuthenticationResponseDTO;
import com.selflearntech.techblogbackend.user.dto.UserDTO;
import com.selflearntech.techblogbackend.user.dto.RegistrationRequestDTO;

public interface IAuthenticationService {

    String registerUser(RegistrationRequestDTO registerDTO);

    AuthenticationResponseDTO authenticateUser(String email, String password);

    UserDTO refreshAccessToken(String refreshToken);
}
