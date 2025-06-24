package com.jphilips.auth.dto.cqrs.query;

import lombok.Builder;

@Builder
public record GetUserByEmailQuery(
        Long headerUserId,
        String email
) {
}
