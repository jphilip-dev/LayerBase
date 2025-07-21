package com.jphilips.analytics.kafka.authEvent;

import com.jphilips.analytics.dto.RegisteredEventLogRequestDto;
import com.jphilips.analytics.dto.cqrs.command.CreateRegisteredEventLogCommand;
import com.jphilips.analytics.service.command.CreateRegisteredEventLogService;
import com.jphilips.shared.spring.kafka.dto.AppEvent;
import com.jphilips.shared.spring.kafka.dto.payload.UserLoggedInPayload;
import com.jphilips.shared.spring.kafka.dto.payload.UserRegisteredPayload;
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

    private final CreateRegisteredEventLogService createRegisteredEventLogService;

    public void handleUserRegistered(AppEvent<?> rawEvent) {

        headerEventHandler.handleCreate(rawEvent);

        UserRegisteredPayload payload = objectMapperHandler.convert(rawEvent.getPayload(), UserRegisteredPayload.class);

        log.info("handling payload");

        // extract and save payload to its own table
        var dto = RegisteredEventLogRequestDto.builder()
                .eventId(rawEvent.getId())
                .userId(payload.getUserId())
                .email(payload.getEmail())
                .name(payload.getName())
                .build();

        var command = CreateRegisteredEventLogCommand.builder()
                .registeredEventLogRequestDto(dto)
                .build();

        createRegisteredEventLogService.execute(command);

    }

    public void handleUserLoggedIn(AppEvent<?> rawEvent) {

        headerEventHandler.handleCreate(rawEvent);

        UserLoggedInPayload payload = objectMapperHandler.convert(rawEvent.getPayload(), UserLoggedInPayload.class);

        // no need to save payload for login, header is enough

    }
}
