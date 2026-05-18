package com.atomquest.goalportal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.PrivateKey;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="employee_email")
    private String employeeEmail;

    private String thrustArea;

    private String title;

    private String description;

    private String uom;

    private Double targetValue;

    private Double weightage;

    private String status;

    private String approvalStatus;

    private Boolean locked = false;

    private String managerComment;

    private Boolean sharedGoal = false;

    private Long parentGoalId;

    private Boolean targetLocked = false;

    private Double actualAchievement = 0.0;

    private Double progressScore;

    private String quarter;

    private String ProgressStatus = "NOT_STARTED";

    private String employeeComment;

    private String managerReviewComment;

    private String calculationType;

    private String weightageApprovalStatus = "PENDING";

    private Double requestedWeightage;

    private Double plannedProgress;

    private String employeeName;



    // GETTERS AND SETTERS

}