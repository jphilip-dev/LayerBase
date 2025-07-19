package com.jphilips.auth.dto;

import lombok.Builder;

@Builder
public record TokenResponseDto(
        String token
) {
}
