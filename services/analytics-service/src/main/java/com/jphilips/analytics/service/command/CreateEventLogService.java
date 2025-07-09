package com.jphilips.analytics.service.command;

import com.jphilips.shared.domain.util.Command;
import com.jphilips.analytics.dto.cqrs.command.CreateEventLogCommand;
import com.jphilips.analytics.dto.mapper.EventLogMapper;
import com.jphilips.analytics.service.EventLogManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateEventLogService implements Command<CreateEventLogCommand, Void> {

    private final EventLogManager eventLogManager;
    private final EventLogMapper eventLogMapper;

    @Override
    public Void execute(CreateEventLogCommand command) {

        var event = eventLogMapper.toEntity(command.eventLogRequestDto());

        eventLogManager.save(event);

        log.info("Header created");

        return null;
    }

}
