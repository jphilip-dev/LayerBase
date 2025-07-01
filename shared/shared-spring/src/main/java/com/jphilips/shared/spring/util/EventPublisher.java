package com.jphilips.shared.spring.util;

import com.jphiilips.shared.domain.dto.kafka.AppEvent;
import com.jphiilips.shared.domain.enums.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@RequiredArgsConstructor
@Service
public class EventPublisher {

    private final KafkaTemplate<String, AppEvent<?>> kafkaTemplate;

    public <T> void publish(EventType type, T payload, String topic) {
        AppEvent<T> event = AppEvent.<T>builder()
                .type(type)
                .timestamp(Instant.now())
                .payload(payload)
                .build();

        kafkaTemplate.send(topic, event);
    }
}

