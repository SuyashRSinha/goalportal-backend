package com.atomquest.goalportal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;

    private String changedBy;

    private String entityType;

    @Setter
    private Long entityId;

    @Setter
    private String description;

    @Setter
    private LocalDateTime changedAt;

    public AuditLog() {
    }

    public Long getId() {
        return id;
    }

    public String getAction() {
        return action;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public String getEntityType() {
        return entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

}