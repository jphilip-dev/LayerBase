package com.jphilips.auth.dto.cqrs.query;

import org.springframework.data.domain.Pageable;

public record GetAllUsersQuery(
        Pageable pageable
) {
}
