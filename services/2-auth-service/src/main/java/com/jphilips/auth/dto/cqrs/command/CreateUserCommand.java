package com.jphilips.auth.dto.cqrs.command;

import com.jphilips.auth.dto.UserRequestDto;

public record CreateUserCommand(
        UserRequestDto userRequestDto
) {
}
