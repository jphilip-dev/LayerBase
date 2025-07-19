package com.jphilips.auth.dto.cqrs.command;

import lombok.Builder;

@Builder
public record RequestOtpCommand(
        String toEmail
) {
}
