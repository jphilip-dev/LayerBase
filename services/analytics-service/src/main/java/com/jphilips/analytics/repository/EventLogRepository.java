package com.jphilips.analytics.repository;

import com.jphilips.analytics.entity.HeaderEventLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventLogRepository extends JpaRepository<HeaderEventLog, UUID> {
}
