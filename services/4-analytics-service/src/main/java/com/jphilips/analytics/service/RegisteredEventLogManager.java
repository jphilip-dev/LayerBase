package com.jphilips.analytics.service;

import com.jphilips.analytics.entity.RegisteredEventLog;
import com.jphilips.analytics.repository.RegisteredEventLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisteredEventLogManager {

    private final RegisteredEventLogRepository registeredEventLogRepository;


    public RegisteredEventLog save (RegisteredEventLog registeredEventLog){
        return registeredEventLogRepository.save(registeredEventLog);
    }

}
