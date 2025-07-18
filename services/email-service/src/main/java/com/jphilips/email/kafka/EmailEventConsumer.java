package com.jphilips.email.kafka;

import com.jphilips.shared.domain.dto.kafka.AppEvent;
import com.jphilips.shared.spring.kafka.service.DLTHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailEventConsumer {

    private final EmailEventHandler emailEventHandler;
    private final DLTHandler dltHandler;

    @KafkaListener(topics = "#{@kafkaTopics.emailEvent}", groupId = "MyGroup")
    public void handleAuthEvent(AppEvent<?> event) {
        switch (event.getType()) {
            case EMAIL_OTP -> emailEventHandler.handleEmailOtp(event);
            default -> log.warn("Unhandled auth event:{}", event.getType());
        }
    }

    @KafkaListener(
            topics = "#{@kafkaTopics.emailEvent}-dlt",
            groupId = "dlt-handler-group",
            containerFactory = "byteArrayKafkaListenerContainerFactory"
    )
    public void handleDLT(byte[] data,
                          @Header(KafkaHeaders.DLT_EXCEPTION_MESSAGE) String error,
                          @Header(KafkaHeaders.RECEIVED_TOPIC) String originalTopic) {

        dltHandler.handle(data, originalTopic, error);
    }
}
