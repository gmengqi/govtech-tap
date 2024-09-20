package com.example.football_championship.utils;

import com.example.football_championship.audit.AuditLog;
import com.example.football_championship.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class AuditLogUtils {

    @Autowired
    private static AuditLogRepository auditLogRepository;

    public static void createAuditLog(String action, String entityName, String details) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setEntityName(entityName);
        log.setDetails(details);
        log.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(log);
    }
}
