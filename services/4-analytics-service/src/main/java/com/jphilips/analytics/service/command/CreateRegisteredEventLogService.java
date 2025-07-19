package com.jphilips.analytics.service.command;

import com.jphilips.analytics.dto.cqrs.command.CreateRegisteredEventLogCommand;
import com.jphilips.analytics.dto.mapper.RegisteredEventLogMapper;
import com.jphilips.analytics.service.HeaderEventLogManager;
import com.jphilips.analytics.service.RegisteredEventLogManager;
import com.jphilips.shared.domain.util.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateRegisteredEventLogService implements Command<CreateRegisteredEventLogCommand, Void> {

    private final RegisteredEventLogManager registeredEventLogManager;
    private final RegisteredEventLogMapper registeredEventLogMapper;

    private final HeaderEventLogManager headerEventLogManager;

    @Override
    public Void execute(CreateRegisteredEventLogCommand command) {

        var event = registeredEventLogMapper.toEntity(command.registeredEventLogRequestDto());

        event.setHeader(headerEventLogManager.validateEventLog(command.registeredEventLogRequestDto().getEventId()));

        registeredEventLogManager.save(event);

        return null;
    }
}
