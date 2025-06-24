package com.jphilips.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginRequestDto {

    @Email(message = "{email.invalid}")
    @NotBlank(message = "{required.notnull}")
    private String email;

    @NotBlank(message = "{required.notnull}")
    @Size(min = 6, message = "{password.tooShort}")
    private String password;
}