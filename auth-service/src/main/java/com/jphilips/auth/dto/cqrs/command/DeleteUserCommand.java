package com.jphilips.auth.dto.cqrs.command;

import lombok.Builder;

@Builder
public record DeleteUserCommand(
        Long headerUserId,
        Long userId
) {
}
