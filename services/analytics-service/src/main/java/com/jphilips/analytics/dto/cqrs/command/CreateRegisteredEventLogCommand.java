package com.jphilips.analytics.dto.cqrs.command;

import com.jphilips.analytics.dto.RegisteredEventLogRequestDto;
import lombok.Builder;

@Builder
public record CreateRegisteredEventLogCommand (
        RegisteredEventLogRequestDto registeredEventLogRequestDto
){
}
