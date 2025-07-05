package com.jphilips.shared.spring.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class KafkaTopics {

    private final String userEvents;
    private final String authEvents;

    public KafkaTopics(
            @Value("${app.kafka.topics.user-events}") String userEvents,
            @Value("${app.kafka.topics.auth-events}") String authEvents
    ) {
        this.userEvents = userEvents;
        this.authEvents = authEvents;
    }
}
