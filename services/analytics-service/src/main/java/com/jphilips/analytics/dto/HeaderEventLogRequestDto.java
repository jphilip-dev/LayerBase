package com.jphilips.analytics.dto;


import com.jphilips.shared.domain.enums.EventType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class HeaderEventLogRequestDto {

    @NotNull
    private UUID eventId;

    @NotNull
    private EventType eventType;

    @NotNull
    private Long userId;

    @NotNull
    private Instant timestamp;
}
