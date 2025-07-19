package com.jphilips.auth.dto.cqrs.command;

import com.jphilips.auth.dto.LoginRequestDto;
import lombok.Builder;

@Builder
public record AuthenticateCommand(
        LoginRequestDto loginRequestDto
) {
}
