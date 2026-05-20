package com.atomquest.goalportal.controller;

import com.atomquest.goalportal.dto.SharedGoalRequest;
import com.atomquest.goalportal.entity.Goal;
import com.atomquest.goalportal.repository.GoalRepository;
import com.atomquest.goalportal.service.GoalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import java.nio.charset.StandardCharsets;

import java.util.List;

@RestController
@RequestMapping("/goals")

public class GoalController {

    @Autowired
    private GoalService goalService;

    @Autowired
    private GoalRepository goalRepository;


    @PostMapping("/create")
    public Goal createGoal(
            @RequestBody Goal goal
    ) {

        return goalService.createGoal(goal);
    }


    @GetMapping
    public List<Goal> getAllGoals() {

        return goalService.getAllGoals();
    }


    @GetMapping("/{email}")
    public List<Goal> getGoals(
            @PathVariable String email
    ) {

        return goalService.getGoalsByEmployee(
                email
        );
    }

    @GetMapping("/employee/{email}")

    public List<Goal> getGoalsByEmployee(

            @PathVariable String email
    ) {

        return goalService
                .getGoalsByEmployee(email);

    }

    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportGoalsCsv() {


        List<Goal> goals = goalRepository.findAll();

        StringBuilder csv = new StringBuilder();

        csv.append(
                "Employee Email,Goal Title,Target Value,Actual Achievement,Progress Status,Approval Status\n"
        );

        for (Goal goal : goals) {

            csv.append(goal.getEmployeeEmail()).append(",")

                    .append(goal.getTitle()).append(",")

                    .append(goal.getTargetValue()).append(",")

                    .append(goal.getActualAchievement()).append(",")

                    .append(goal.getProgressStatus()).append(",")

                    .append(goal.getApprovalStatus()).append("\n");
        }

        byte[] csvBytes =
                csv.toString()
                        .getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()

                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=kpi-report.csv"
                )

                .contentType(MediaType.TEXT_PLAIN)

                .body(csvBytes);
    }

    @GetMapping("/governance-tracking")
    public List<Map<String, Object>> getGovernanceTracking() {

        List<Goal> goals =
                goalRepository.findAll();

        List<Map<String, Object>> response =
                new ArrayList<>();

        for (Goal goal : goals) {

            Map<String, Object> data =
                    new HashMap<>();


            data.put(
                    "employeeEmail",
                    goal.getEmployeeEmail()
            );

            data.put(
                    "goalTitle",
                    goal.getTitle()
            );

            data.put(
                    "quarter",
                    goal.getQuarter()
            );
            boolean employeeCompleted =

                    goal.getActualAchievement() != null;

            data.put(
                    "employeeCompleted",
                    employeeCompleted
            );
            boolean managerReviewed =

        goal.getManagerReviewComment() != null

        &&

        !goal.getManagerReviewComment()
                .trim()
                .isEmpty();

            data.put(
                    "managerReviewed",
                    managerReviewed
            );

            data.put(
                    "approvalStatus",
                    goal.getApprovalStatus()
            );
            String status;

            if (

                    employeeCompleted

                            &&

                            managerReviewed

            ) {

                status = "COMPLETED";
            }

            else if (

                    employeeCompleted

            ) {

                status = "MANAGER_PENDING";
            }

            else {

                status = "PENDING";
            }

            data.put("status", status);

            response.add(data);
        }

        return response;
    }

    @GetMapping("/governance-summary")
    public Map<String, Object> getGovernanceSummary() {

        List<Goal> goals =
                goalRepository.findAll();

        long completedCheckins =
                goals.stream()

                        .filter(goal ->

                                goal.getPlannedProgress() != null

                                        &&

                                        goal.getActualAchievement() != null
                        )

                        .count();

        long pendingCheckins =
                goals.size() - completedCheckins;

        long completedGoals =
                goals.stream()

                        .filter(goal ->

                                "COMPLETED".equalsIgnoreCase(
                                        goal.getProgressStatus()
                                )
                        )

                        .count();

        long onTrackGoals =
                goals.stream()

                        .filter(goal ->

                                "ON_TRACK".equalsIgnoreCase(
                                        goal.getProgressStatus()
                                )
                        )

                        .count();

        long delayedGoals =
                goals.stream()

                        .filter(goal ->

                                "DELAYED".equalsIgnoreCase(
                                        goal.getProgressStatus()
                                )
                        )

                        .count();

        double completionRate =

                goals.isEmpty()

                        ? 0

                        : ((double) completedCheckins
                           / goals.size()) * 100;

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "totalGoals",
                goals.size()
        );

        response.put(
                "completedCheckins",
                completedCheckins
        );

        response.put(
                "pendingCheckins",
                pendingCheckins
        );

        response.put(
                "completedGoals",
                completedGoals
        );

        response.put(
                "onTrackGoals",
                onTrackGoals
        );

        response.put(
                "delayedGoals",
                delayedGoals
        );

        response.put(
                "completionRate",
                completionRate
        );

        return response;
    }
    @PutMapping("/approve/{id}")
    public Goal approveGoal(
            @PathVariable Long id
    ) {

        Goal goal = goalService.getGoalById(id);

        goal.setApprovalStatus("APPROVED");

        goal.setLocked(true);

        return goalService.saveGoal(goal);
    }

    @PutMapping("/unlock/{id}")
    public Goal unlockGoal(
            @PathVariable Long id
    ) {

        Goal goal = goalService.getGoalById(id);

        goal.setLocked(false);

        return goalService.saveGoal(goal);
    }
    @PutMapping("/reject/{id}")
    public Goal rejectGoal(
            @PathVariable Long id
    ) {

        Goal goal = goalService.getGoalById(id);

        goal.setApprovalStatus("REJECTED");

        return goalService.saveGoal(goal);
    }
    @PutMapping("/rework/{id}")
    public Goal returnForRework(

            @PathVariable Long id,

            @RequestBody Goal updatedGoal
    ) {

        Goal goal = goalService.getGoalById(id);

        goal.setApprovalStatus("REWORK");

        goal.setManagerComment(
                updatedGoal.getManagerComment()
        );

        return goalService.saveGoal(goal);
    }
    @PutMapping("/checkin/{id}")
    public Goal updateQuarterlyCheckin(

            @PathVariable Long id,

            @RequestBody Goal updatedGoal
    ) {

        return goalService.updateQuarterlyCheckin(
                id,
                updatedGoal
        );
    }
    @PutMapping("/manager-review/{id}")
    public Goal updatedManagerReview(

            @PathVariable Long id,

            @RequestBody Goal updatedGoal
    ) {

        return goalService.updateManagerReview(
                id,
                updatedGoal
        );
    }
    @PostMapping("/shared")
    public String createSharedGoal(

            @RequestBody SharedGoalRequest request
    ) {

        goalService.shareGoalToEmployees(
                request.getParentGoalId(),
                request.getEmployeeEmails()
        );

        return "Shared Goal created";
    }

    @PutMapping("/shared-weightage/{id}")
    public Goal updateSharedGoalWeightage(

            @PathVariable Long id,

            @RequestParam Double weightage

    ) {

        return goalService
                .updateSharedGoalWeightage(
                        id,
                        weightage
                );
    }
    @PutMapping("/update-weightage/{id}")

    public Goal updateWeightage(

            @PathVariable Long id,

            @RequestBody Goal updatedGoal
    ) {

        Goal goal = goalService.getGoalById(id);

        goal.setWeightage(
                updatedGoal.getWeightage()
        );

        return goalService.saveGoal(goal);
    }
}
