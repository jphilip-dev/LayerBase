package com.jphiilips.shared.domain.dto.kafka;

import com.jphiilips.shared.domain.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppEvent<T> {
    private EventType type;
    private Instant timestamp;
    private T payload;
}