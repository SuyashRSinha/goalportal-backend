package com.atomquest.goalportal.dto;

import java.util.List;

public class SharedGoalRequest {

    private Long parentGoalId;

    private List<String> employeeEmails;

    public Long getParentGoalId() {
        return parentGoalId;
    }

    public void setParentGoalId(Long parentGoalId) {
        this.parentGoalId = parentGoalId;
    }

    public List<String> getEmployeeEmails() {
        return employeeEmails;
    }

    public void setEmployeeEmails(List<String> employeeEmails) {
        this.employeeEmails = employeeEmails;
    }
}