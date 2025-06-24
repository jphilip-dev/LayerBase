package com.jphilips.auth.dto.cqrs.query;

public record GetUserByIdQuery(
        Long headerUserId,
        Long userId
) {
}
