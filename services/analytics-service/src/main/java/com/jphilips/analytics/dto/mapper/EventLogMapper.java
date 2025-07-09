package com.jphilips.analytics.dto.mapper;

import com.jphilips.shared.domain.dto.kafka.AppEvent;
import com.jphilips.analytics.dto.EventLogRequestDto;
import com.jphilips.analytics.entity.EventLog;
import org.springframework.stereotype.Component;

@Component
public class EventLogMapper {
    public EventLog toEntity(EventLogRequestDto dto){
        return EventLog.builder()
                .eventId(dto.getEventId())
                .userId(dto.getUserId())
                .eventType(dto.getEventType())
                .timestamp(dto.getTimestamp())
                .build();
    }

    public EventLogRequestDto toDto(AppEvent<?> rawEvent){
        return EventLogRequestDto.builder()
                .eventId(rawEvent.getId())
                .userId(rawEvent.getUserId())
                .eventType(rawEvent.getType())
                .timestamp(rawEvent.getTimestamp())
                .build();
    }
}
