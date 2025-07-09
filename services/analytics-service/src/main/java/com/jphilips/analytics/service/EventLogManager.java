package com.jphilips.analytics.service;

import com.jphilips.shared.domain.exception.errorcode.AnalyticsErrorCode;
import com.jphilips.analytics.entity.HeaderEventLog;
import com.jphilips.analytics.exception.custom.EventLogNotFoundException;
import com.jphilips.analytics.repository.EventLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventLogManager  {

    private final EventLogRepository eventLogRepository;

    public HeaderEventLog save(HeaderEventLog headerEventLog){
        log.info("Saving Event Log: Event Id - {}, Event Type - {}", headerEventLog.getEventId(), headerEventLog.getEventType());
        return eventLogRepository.save(headerEventLog);
    }

    public void delete(UUID uuid){
        var eventLog = validateEventLog(uuid);
        log.info("Deleting Event Log: Event Id - {}, Event Type - {}", eventLog.getEventId(), eventLog.getEventType());
        eventLogRepository.delete(eventLog);
    }

    public HeaderEventLog validateEventLog(UUID uuid){
        return eventLogRepository.findById(uuid)
                .orElseThrow(() -> new EventLogNotFoundException(AnalyticsErrorCode.NOT_FOUND));
    }
}
