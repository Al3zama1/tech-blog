package com.selflearntech.techblogbackend.user.dto;

import com.selflearntech.techblogbackend.exception.ErrorMessages;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationRequestDTO {
    @NotBlank
    String firstName;

    @NotBlank
    String lastName;

    @NotBlank
    @Email
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%]).{8,15}$",
            message = ErrorMessages.PASSWORD_CONSTRAINT)
    private String password;

    @NotNull
    @Size(min = 8, max = 15)
    private String verifyPassword;
}
