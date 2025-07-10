package com.jphilips.analytics.service.command;

import com.jphilips.shared.domain.util.Command;
import com.jphilips.analytics.dto.cqrs.command.CreateEventLogCommand;
import com.jphilips.analytics.dto.mapper.HeaderEventLogMapper;
import com.jphilips.analytics.service.HeaderEventLogManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateHeaderEventLogService implements Command<CreateEventLogCommand, Void> {

    private final HeaderEventLogManager headerEventLogManager;
    private final HeaderEventLogMapper headerEventLogMapper;

    @Override
    public Void execute(CreateEventLogCommand command) {

        var event = headerEventLogMapper.toEntity(command.headerEventLogRequestDto());

        headerEventLogManager.save(event);

//        log.info("Header created");

        return null;
    }

}
