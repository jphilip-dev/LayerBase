package com.jphilips.userdetails.dto.cqrs;

import lombok.Builder;

@Builder
public record DeleteUserDetailsCommand (
        Long userDetailsId
) {
}
