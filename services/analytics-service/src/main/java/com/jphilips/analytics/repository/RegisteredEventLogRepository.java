package com.jphilips.analytics.repository;

import com.jphilips.analytics.entity.RegisteredEventLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisteredEventLogRepository extends JpaRepository<RegisteredEventLog, Long> {
}
