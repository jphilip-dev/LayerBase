package com.jphilips.analytics.kafka.headerEvent;

import com.jphilips.shared.domain.dto.kafka.AppEvent;
import com.jphilips.analytics.dto.cqrs.command.CreateEventLogCommand;
import com.jphilips.analytics.dto.mapper.HeaderEventLogMapper;
import com.jphilips.analytics.service.command.CreateHeaderEventLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeaderEventHandler {

    private final CreateHeaderEventLogService createHeaderEventLogService;
    private final HeaderEventLogMapper headerEventLogMapper;

    public void handleCreate(AppEvent<?> rawEvent) {

        var dto = headerEventLogMapper.toDto(rawEvent);

        var command = CreateEventLogCommand.builder()
                .headerEventLogRequestDto(dto)
                .build();

        createHeaderEventLogService.execute(command);
    }
}
