package com.jphilips.auth.dto.cqrs.query;

import lombok.Builder;

@Builder
public record GetUserByIdQuery(
        Long headerUserId,
        Long userId
) {
}
