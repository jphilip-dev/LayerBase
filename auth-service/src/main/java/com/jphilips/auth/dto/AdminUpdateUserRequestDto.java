package com.jphilips.auth.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminUpdateUserRequestDto {

    @Valid
    private UserRequestDto user;

    @NotNull(message = "{required.notnull}")
    private Boolean isActive;

    @NotNull(message = "{required.notnull}")
    private Boolean isAdmin;
}
