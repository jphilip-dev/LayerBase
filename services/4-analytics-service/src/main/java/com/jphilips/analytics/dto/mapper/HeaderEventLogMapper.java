package com.jphilips.analytics.dto.mapper;

import com.jphilips.shared.domain.dto.kafka.AppEvent;
import com.jphilips.analytics.dto.HeaderEventLogRequestDto;
import com.jphilips.analytics.entity.HeaderEventLog;
import org.springframework.stereotype.Component;

@Component
public class HeaderEventLogMapper {
    public HeaderEventLog toEntity(HeaderEventLogRequestDto dto){
        return HeaderEventLog.builder()
                .eventId(dto.getEventId())
                .userId(dto.getUserId())
                .eventType(dto.getEventType())
                .timestamp(dto.getTimestamp())
                .build();
    }

    public HeaderEventLogRequestDto toDto(AppEvent<?> rawEvent){
        return HeaderEventLogRequestDto.builder()
                .eventId(rawEvent.getId())
                .userId(rawEvent.getUserId())
                .eventType(rawEvent.getType())
                .timestamp(rawEvent.getTimestamp())
                .build();
    }
}
