package com.jphilips.shared.spring.kafka.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class KafkaTopics {

    private final String userEvent;
    private final String authEvent;
    private final String emailEvent;

    public KafkaTopics(
            @Value("${app.kafka.topics.user-events}") String userEvent,
            @Value("${app.kafka.topics.auth-events}") String authEvent,
            @Value("${app.kafka.topics.email-events}") String emailEvent
    ) {
        this.userEvent = userEvent;
        this.authEvent = authEvent;
        this.emailEvent = emailEvent;
    }
}
