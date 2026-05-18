package com.atomquest.goalportal.service;

import com.atomquest.goalportal.entity.Goal;
import com.atomquest.goalportal.repository.GoalRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.atomquest.goalportal.service.AuditService;
import java.time.LocalDate;
import java.time.Month;

import java.util.List;

@Service
public class GoalService {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private AuditService auditService;

    @Autowired
    private ProgressCalculationService progressCalculationService;

    private boolean isQuarterWindowOpen(String quarter){

        return true;
      /*  Month currentMonth = LocalDate.now().getMonth();

        return switch (quarter) {
            case "Q1" -> currentMonth == Month.JULY;
            case "Q2" -> currentMonth == Month.OCTOBER;
            case "Q3" -> currentMonth == Month.JANUARY;
            case "Q4" -> currentMonth == Month.MARCH
                    || currentMonth == Month.APRIL;
            default -> false;
        };*/
    }


    public Goal createGoal(Goal goal) {

        LocalDate today = LocalDate.now();

        Month currentMonth = today.getMonth();

        int currentDay = today.getDayOfMonth();

        if (
                currentMonth.getValue() < 5
        ) {

            throw new RuntimeException(
                    "Goal creation opens from May 1"
            );
        }

        List<Goal> existingGoals =
                goalRepository.findByEmployeeEmail(
                        goal.getEmployeeEmail()
                );

        if (existingGoals.size() >= 8) {

            throw new RuntimeException(
                    "Maximum 8 goals allowed"
            );
        }

        if (goal.getWeightage() < 10) {

            throw new RuntimeException(
                    "Minimum weightage is 10%"
            );
        }

        double totalWeightage = existingGoals
                .stream()
                .mapToDouble(Goal::getWeightage)
                .sum();

        if (totalWeightage + goal.getWeightage() > 100) {

            throw new RuntimeException(
                    "Total weightage cannot exceed 100%"
            );
        }

        Goal savedGoal = goalRepository.save(goal);
        auditService.logAction(
                "CREATE_GOAL",
                goal.getEmployeeEmail(),
                "GOAL",
                savedGoal.getId(),
                "Goal created: " +savedGoal.getTitle()
        );
        return savedGoal;
    }

    public List<Goal> getAllGoals(){

        return goalRepository.findAll();
    }

    public List<Goal> getGoalsByEmployee(
            String email
    ) {

        return goalRepository.findByEmployeeEmail(
                email
        );
    }

    public Goal getGoalById(Long id) {

        return goalRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Goal not found"
                        )
                );
    }

    public Goal saveGoal(Goal goal) {

        return goalRepository.save(goal);
    }

    public void shareGoalToEmployees(

            Long parentGoalId,

            List<String> employeeEmails

    ) {

        // FETCH EXISTING APPROVED GOAL

        Goal parentGoal = goalRepository
                .findById(parentGoalId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Goal Not Found"
                        )
                );

        // CREATE CHILD GOALS

        for (String email : employeeEmails) {

            Goal childGoal = new Goal();

            childGoal.setSharedGoal(true);
            childGoal.setParentGoalId(parentGoal.getId());
            childGoal.setTargetLocked(true);

            childGoal.setEmployeeEmail(email);

            childGoal.setThrustArea(
                    parentGoal.getThrustArea()
            );

            childGoal.setTitle(
                    parentGoal.getTitle()
            );

            childGoal.setDescription(
                    parentGoal.getDescription()
            );

            childGoal.setUom(
                    parentGoal.getUom()
            );

            childGoal.setTargetValue(
                    parentGoal.getTargetValue()
            );

            childGoal.setWeightage(
                    parentGoal.getWeightage()
            );

            childGoal.setStatus(
                    "NOT_STARTED"
            );

            childGoal.setApprovalStatus(
                    "APPROVED"
            );

            childGoal.setLocked(false);

            childGoal.setManagerComment("");

            // SHARED KPI SETTINGS

            childGoal.setSharedGoal(true);

            childGoal.setParentGoalId(
                    parentGoal.getId()
            );

            childGoal.setTargetLocked(true);

            goalRepository.save(childGoal);
        }
    }
    public Goal updateQuarterlyCheckin(
            Long goalId,
            Goal updateGoal
    ){
        Goal existingGoal= goalRepository.findById(goalId).orElseThrow(()->new
                RuntimeException("Goal not found"));

        if(!isQuarterWindowOpen(updateGoal.getQuarter())){
            throw new RuntimeException("check-in window is closed for " + updateGoal.getQuarter());
        }
        existingGoal.setPlannedProgress(updateGoal.getPlannedProgress());

        existingGoal.setActualAchievement(
                updateGoal.getActualAchievement()
        );
        existingGoal.setProgressScore(
                progressCalculationService.calculateProgress(existingGoal)
        );

        existingGoal.setQuarter(updateGoal.getQuarter());

        existingGoal.setProgressStatus(updateGoal.getProgressStatus());

        existingGoal.setEmployeeComment(updateGoal.getEmployeeComment());

        Goal updated = goalRepository.save(existingGoal);
        auditService.logAction(
                "QUARTERLY_CHECKIN",
                existingGoal.getEmployeeEmail(),
                "GOAL",
                existingGoal.getId(),

                "Quarter updated: " + existingGoal.getQuarter()
        );
        return updated;
    }

    /*public Goal updateManagerReview(
            Long goalId,
            Goal updatedGoal
    ){
        Goal existingGoal = goalRepository.findById(goalId).orElseThrow(()->new RuntimeException("Goal Not Found"));
        existingGoal.setManagerReviewComment(updatedGoal.getManagerReviewComment());

        Goal updated = goalRepository.save(existingGoal);

        auditService.logAction(

                "MANAGER_REVIEW",
                "MANAGER",
                "GOAL",
                existingGoal.getId(),
                "manager reviewed goal"
        );
        return updated;
    }*/

    public Goal updateManagerReview(
            Long goalId,
            Goal updatedGoal
    ){

        Goal existingGoal =
                goalRepository.findById(goalId)
                        .orElseThrow(() ->
                                new RuntimeException("Goal Not Found"));

        existingGoal.setManagerReviewComment(
                updatedGoal.getManagerReviewComment()
        );

        // UPDATE APPROVAL STATUS

        existingGoal.setApprovalStatus(
                updatedGoal.getApprovalStatus()
        );

        Goal updated =
                goalRepository.save(existingGoal);

        auditService.logAction(

                "MANAGER_REVIEW",
                "MANAGER",
                "GOAL",
                existingGoal.getId(),
                "manager reviewed goal"

        );

        return updated;
    }

    public Goal updateSharedGoalWeightage(
            Long goalId,
            Double newWeightage
    ) {

        Goal childGoal = goalRepository.findById(goalId)
                .orElseThrow(() ->
                        new RuntimeException("Goal Not Found"));

        // ONLY SHARED KPI CAN UPDATE

        if (
                childGoal.getSharedGoal() == null
                        || !childGoal.getSharedGoal()
        ) {

            throw new RuntimeException(
                    "Only shared goals can update weightage"
            );
        }

        // UPDATE CHILD KPI WEIGHTAGE

        childGoal.setWeightage(newWeightage);

        Goal updatedChild =
                goalRepository.save(childGoal);

        // UPDATE PARENT KPI WEIGHTAGE

        Goal parentGoal =
                goalRepository.findById(
                        childGoal.getParentGoalId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Parent Goal Not Found"
                        )
                );

        parentGoal.setWeightage(newWeightage);

        goalRepository.save(parentGoal);

        // UPDATE ALL OTHER CHILD GOALS

        List<Goal> allGoals =
                goalRepository.findAll();

        for (Goal goal : allGoals) {

            if (

                    goal.getSharedGoal() != null

                            &&

                            goal.getSharedGoal()

                            &&

                            goal.getParentGoalId() != null

                            &&

                            goal.getParentGoalId()
                                    .equals(parentGoal.getId())

            ) {

                goal.setWeightage(newWeightage);

                goalRepository.save(goal);
            }
        }

        auditService.logAction(

                "UPDATE_SHARED_WEIGHTAGE",

                childGoal.getEmployeeEmail(),

                "GOAL",

                childGoal.getId(),

                "Updated shared KPI weightage to "
                        + newWeightage

        );

        return updatedChild;
    }



}
