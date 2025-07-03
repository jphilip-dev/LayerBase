package com.jphilips.analytics.service;

import com.jphiilips.shared.domain.exception.errorcode.AnalyticsErrorCode;
import com.jphilips.analytics.entity.EventLog;
import com.jphilips.analytics.exception.custom.EventLogNotFoundException;
import com.jphilips.analytics.repository.EventLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventLogManager  {

    private final EventLogRepository eventLogRepository;

    public EventLog save(EventLog eventLog){
        return eventLogRepository.save(eventLog);
    }

    public void delete(UUID uuid){
        var eventLog = validateEventLog(uuid);
        eventLogRepository.delete(eventLog);
    }

    public EventLog validateEventLog(UUID uuid){
        return eventLogRepository.findById(uuid)
                .orElseThrow(() -> new EventLogNotFoundException(AnalyticsErrorCode.NOT_FOUND));
    }
}
