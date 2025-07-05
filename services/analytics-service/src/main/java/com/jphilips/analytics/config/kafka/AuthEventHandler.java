package com.jphilips.analytics.config.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jphiilips.shared.domain.dto.kafka.AppEvent;
import com.jphiilips.shared.domain.dto.kafka.payload.UserLoggedInPayload;
import com.jphiilips.shared.domain.dto.kafka.payload.UserRegisteredPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthEventHandler {
    private final ObjectMapper objectMapper;

    public void handleUserRegistered(AppEvent<?> rawEvent) {
        UserRegisteredPayload payload = objectMapper.convertValue(rawEvent.getPayload(), UserRegisteredPayload.class);
        System.out.println("👤 Registered: " + payload.getEmail());
    }

    public void handleUserLoggedIn(AppEvent<?> rawEvent) {
        UserLoggedInPayload payload = objectMapper.convertValue(rawEvent.getPayload(), UserLoggedInPayload.class);
        System.out.println("✅ Logged in: " + payload.getUserId());
    }

}
