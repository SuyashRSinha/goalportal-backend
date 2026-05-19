package com.atomquest.goalportal.controller;

import com.atomquest.goalportal.entity.AuditLog;
import com.atomquest.goalportal.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/audit")

public class AuditController {
    @Autowired
    private AuditLogRepository auditLogRepository;
    @GetMapping
    public List<AuditLog> getAllLogs(){
        return
                auditLogRepository.findAll();
    }
}
