package com.jphilips.analytics.kafka.authEvent;

import com.jphilips.shared.domain.dto.kafka.AppEvent;
import com.jphilips.shared.domain.dto.kafka.payload.UserLoggedInPayload;
import com.jphilips.shared.domain.dto.kafka.payload.UserRegisteredPayload;
import com.jphilips.analytics.kafka.headerEvent.HeaderEventHandler;
import com.jphilips.shared.spring.util.ObjectMapperHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthEventHandler {

    private final ObjectMapperHandler objectMapperHandler;
    private final HeaderEventHandler headerEventHandler;

    public void handleUserRegistered(AppEvent<?> rawEvent) {

        headerEventHandler.handleCreate(rawEvent);

        UserRegisteredPayload payload = objectMapperHandler.convert(rawEvent.getPayload(), UserRegisteredPayload.class);

        log.info("handling payload");
        // extract and save payload to its own table

    }

    public void handleUserLoggedIn(AppEvent<?> rawEvent) {

        headerEventHandler.handleCreate(rawEvent);

        UserLoggedInPayload payload = objectMapperHandler.convert(rawEvent.getPayload(), UserLoggedInPayload.class);

        // no need to save payload for login, header is enough

    }
}
