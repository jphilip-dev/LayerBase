package com.jphilips.analytics.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RegisteredEventLogRequestDto {
    @NotNull
    private UUID eventId;

    @NotNull
    private Long userId;

    @NotNull
    private String email;

    @NotNull
    private String name;
}
