package com.atomquest.goalportal.service;

import com.atomquest.goalportal.entity.Goal;
import org.springframework.stereotype.Service;

@Service
public class ProgressCalculationService {

    public double calculateProgress(Goal goal) {

        if (goal.getActualAchievement() == null ||
                goal.getTargetValue() == null) {

            return 0;
        }

        double achievement = goal.getActualAchievement();
        double target = goal.getTargetValue();

        if (target == 0) {
            return 0;
        }

        String calculationType = goal.getCalculationType();
        if(calculationType == null){
            return 0;
        }

        switch (calculationType) {

            case "HIGHER_IS_BETTER":

                return Math.min(
                        (achievement / target) * 100,
                        100
                );

            case "LOWER_IS_BETTER":

                if (achievement == 0) {
                    return 100;
                }

                return Math.min(
                        (target / achievement) * 100,
                        100
                );

            case "ZERO_BASED":

                return achievement == 0 ? 100 : 0;

            default:

                return 0;
        }
    }
}