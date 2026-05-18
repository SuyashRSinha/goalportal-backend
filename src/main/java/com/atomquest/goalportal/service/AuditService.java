package com.atomquest.goalportal.service;

import com.atomquest.goalportal.entity.AuditLog;
import com.atomquest.goalportal.repository.AuditLogRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    public void logAction(

            String action,

            String changedBy,

            String entityType,

            Long entityId,

            String description

    ) {

        AuditLog auditLog = new AuditLog();

        auditLog.setAction(action);

        auditLog.setChangedBy(changedBy);

        auditLog.setEntityType(entityType);

        auditLog.setEntityId(entityId);

        auditLog.setDescription(description);

        auditLog.setChangedAt(LocalDateTime.now());

        auditLogRepository.save(auditLog);
    }
}