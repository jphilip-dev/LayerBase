package com.jphilips.auth.dto.cqrs.command;

import com.jphilips.auth.dto.UserRequestDto;
import lombok.Builder;

@Builder
public record UpdateUserCommand(
        Long headerUserId,
        Long userId,
        UserRequestDto userRequestDto,
        Boolean isActive,
        Boolean isAdmin
) {
}
