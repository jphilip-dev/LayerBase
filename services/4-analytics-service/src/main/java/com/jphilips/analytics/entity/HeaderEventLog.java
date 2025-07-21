package com.jphilips.analytics.entity;

import com.jphilips.shared.spring.kafka.enums.EventType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "header_event_logs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HeaderEventLog {
    @Id
    private UUID eventId;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Instant timestamp;
}

