package com.jphilips.shared.domain.dto.kafka;

import com.jphilips.shared.domain.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppEvent<T> {
    private UUID id;
    private Long userId;
    private EventType type;
    private Instant timestamp;
    private T payload;
}