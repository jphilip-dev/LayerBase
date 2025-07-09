package com.jphilips.analytics.dto.cqrs.command;


import com.jphilips.analytics.dto.EventLogRequestDto;
import lombok.Builder;

@Builder
public record CreateEventLogCommand (
        EventLogRequestDto eventLogRequestDto
){
}
