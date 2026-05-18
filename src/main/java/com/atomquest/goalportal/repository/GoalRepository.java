package com.atomquest.goalportal.repository;

import com.atomquest.goalportal.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal,Long> {

    List<Goal> findByEmployeeEmail(String employeeEmail);
}
