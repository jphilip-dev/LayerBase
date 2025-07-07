package com.jphilips.shared.spring.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class KafkaTopics {

    private final String userEvent;
    private final String authEvent;

    public KafkaTopics(
            @Value("${app.kafka.topics.user-events}") String userEvent,
            @Value("${app.kafka.topics.auth-events}") String authEvent
    ) {
        this.userEvent = userEvent;
        this.authEvent = authEvent;
    }
}
