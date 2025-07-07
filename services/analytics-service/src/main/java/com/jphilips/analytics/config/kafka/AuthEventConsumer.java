package com.jphilips.analytics.config.kafka;

import com.jphiilips.shared.domain.dto.kafka.AppEvent;
import com.jphilips.shared.spring.util.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthEventConsumer {

    private final KafkaTopics kafkaTopics;
    private final AuthEventHandler authEventHandler;

    @KafkaListener(topics = "#{@kafkaTopics.authEvent}", groupId = "MyGroup")
    public void handleAuthEvent(AppEvent<?> event) {
        switch (event.getType()) {
            case USER_REGISTERED -> authEventHandler.handleUserRegistered(event);
            case USER_LOGGED_IN -> authEventHandler.handleUserLoggedIn(event);
            default -> log.warn("Unhandled auth event:{}", event.getType() );
        }
    }
}
