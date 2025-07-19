package com.jphilips.analytics.dto.mapper;

import com.jphilips.analytics.dto.RegisteredEventLogRequestDto;
import com.jphilips.analytics.entity.RegisteredEventLog;
import org.springframework.stereotype.Component;

@Component
public class RegisteredEventLogMapper {
    public RegisteredEventLog toEntity(RegisteredEventLogRequestDto registeredEventLogRequestDto){
        return RegisteredEventLog.builder()
                .userId(registeredEventLogRequestDto.getUserId())
                .email(registeredEventLogRequestDto.getEmail())
                .name(registeredEventLogRequestDto.getName())
                .build();
    }

}
