package com.jphilips.analytics.kafka.headerEvent;

import com.jphilips.shared.domain.dto.kafka.AppEvent;
import com.jphilips.analytics.dto.cqrs.command.CreateEventLogCommand;
import com.jphilips.analytics.dto.mapper.EventLogMapper;
import com.jphilips.analytics.service.command.CreateHeaderEventLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeaderEventHandler {

    private final CreateHeaderEventLogService createHeaderEventLogService;
    private final EventLogMapper eventLogMapper;

    public void handleCreate(AppEvent<?> rawEvent) {

        var dto = eventLogMapper.toDto(rawEvent);

        var command = CreateEventLogCommand.builder()
                .eventLogRequestDto(dto)
                .build();

        createHeaderEventLogService.execute(command);
    }
}
