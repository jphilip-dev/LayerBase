package com.jphilips.shared.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@Builder
public record UserResponseDto(
        Long id,
        String email,
        Boolean isActive,
        List<String> roles,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String requestId) {
}
