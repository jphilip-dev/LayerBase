package com.jphilips.auth.dto;


import com.jphilips.auth.validator.UniqueEmail;
import com.jphilips.shared.domain.validator.groups.OnCreate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequestDto {

    @Email(message = "{email.invalid}")
    @NotBlank(message = "{required.notnull}")
    @UniqueEmail(groups = OnCreate.class, message = "{email.taken}")
    private String email;

    @NotBlank(message = "{required.notnull}")
    @Size(min = 6, message = "{password.tooShort}")
    private String password;

    @NotBlank(message = "{required.notnull}")
    private String name;

    private String address;

    @NotNull(message = "{required.notnull}")
    private LocalDate birthDate;

}

