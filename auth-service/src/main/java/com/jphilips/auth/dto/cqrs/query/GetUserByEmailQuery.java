package com.jphilips.auth.dto.cqrs.query;

public record GetUserByEmailQuery(
        Long headerUserId,
        String email
) {
}
