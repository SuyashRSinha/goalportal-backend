package com.atomquest.goalportal.repository;
import com.atomquest.goalportal.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog,Long> {
}
