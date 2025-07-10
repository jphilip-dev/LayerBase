package com.jphilips.analytics.dto.cqrs.command;


import com.jphilips.analytics.dto.HeaderEventLogRequestDto;
import lombok.Builder;

@Builder
public record CreateEventLogCommand (
        HeaderEventLogRequestDto headerEventLogRequestDto
){
}
