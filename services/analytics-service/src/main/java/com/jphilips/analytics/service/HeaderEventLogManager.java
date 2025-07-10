package com.jphilips.analytics.service;

import com.jphilips.analytics.entity.HeaderEventLog;
import com.jphilips.analytics.repository.HeaderEventLogRepository;
import com.jphilips.shared.domain.exception.errorcode.AnalyticsErrorCode;
import com.jphilips.analytics.exception.custom.EventLogNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class HeaderEventLogManager  {

    private final HeaderEventLogRepository headerEventLogRepository;

    public HeaderEventLog save(HeaderEventLog eventLog){
        log.info("Saving Event Log: Event Id - {}, Event Type - {}", eventLog.getEventId(), eventLog.getEventType());
        return headerEventLogRepository.save(eventLog);
    }

    public void delete(UUID uuid){
        var eventLog = validateEventLog(uuid);
        log.info("Deleting Event Log: Event Id - {}, Event Type - {}", eventLog.getEventId(), eventLog.getEventType());
        headerEventLogRepository.delete(eventLog);
    }

    public HeaderEventLog validateEventLog(UUID uuid){
        return headerEventLogRepository.findById(uuid)
                .orElseThrow(() -> new EventLogNotFoundException(AnalyticsErrorCode.NOT_FOUND));
    }
}
