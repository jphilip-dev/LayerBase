package com.jphilips.shared.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserDetailsResponseDto(
        Long id,
        String name,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String address,
        LocalDate birthDate
) {
}
