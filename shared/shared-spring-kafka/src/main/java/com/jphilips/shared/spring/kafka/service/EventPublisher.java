package com.jphilips.shared.spring.kafka.service;

import com.jphilips.shared.spring.kafka.dto.AppEvent;
import com.jphilips.shared.spring.kafka.dto.BasePayload;
import com.jphilips.shared.spring.kafka.enums.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public <T extends BasePayload> void publish(EventType type, T payload, String topic, String requestId) {
        UUID uuid = UUID.fromString(requestId);
        AppEvent<T> event = AppEvent.<T>builder()
                .id(uuid)
                .userId(payload.getUserId())
                .type(type)
                .timestamp(Instant.now())
                .payload(payload)
                .build();

        kafkaTemplate.send(topic, event);
    }
}


