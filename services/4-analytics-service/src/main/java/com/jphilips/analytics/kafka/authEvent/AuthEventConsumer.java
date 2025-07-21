package com.jphilips.analytics.kafka.authEvent;

import com.jphilips.shared.spring.kafka.dto.AppEvent;
import com.jphilips.shared.spring.kafka.service.DLTHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;



@Slf4j
@Component
public class AuthEventConsumer {

    private final AuthEventHandler authEventHandler;
    private final DLTHandler dltHandler;

    public AuthEventConsumer(AuthEventHandler authEventHandler, DLTHandler DLTHandler1) {
        this.authEventHandler = authEventHandler;
        this.dltHandler = DLTHandler1;
    }

    @KafkaListener(topics = "#{@kafkaTopics.authEvent}", groupId = "MyGroup")
    public void handleAuthEvent(AppEvent<?> event) {
        switch (event.getType()) {
            case USER_REGISTERED -> authEventHandler.handleUserRegistered(event);
            case USER_LOGGED_IN -> authEventHandler.handleUserLoggedIn(event);
            default -> log.warn("Unhandled auth event:{}", event.getType());
        }
    }

    @KafkaListener(
            topics = "#{@kafkaTopics.authEvent}-dlt",
            groupId = "dlt-handler-group",
            containerFactory = "byteArrayKafkaListenerContainerFactory"
    )
    public void handleDLT(byte[] data,
                          @Header(KafkaHeaders.DLT_EXCEPTION_MESSAGE) String error,
                          @Header(KafkaHeaders.RECEIVED_TOPIC) String originalTopic) {

        dltHandler.handle(data, originalTopic, error);
    }

}
