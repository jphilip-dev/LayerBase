package com.jphilips.userdetails.dto.cqrs;

import lombok.Builder;

@Builder
public record GetUserDetailsByIdQuery(
        Long headerUserId,
        Long userDetailsId
) {
}
