package com.jphilips.shared.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record UserResponseDto(
        Long id,
        String email,
        Boolean isActive,
        List<String> roles) {
}
