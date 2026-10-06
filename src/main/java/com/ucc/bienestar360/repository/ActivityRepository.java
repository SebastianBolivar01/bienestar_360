package com.ucc.bienestar360.repository;

import com.ucc.bienestar360.model.Activity;
import com.ucc.bienestar360.model.ActivityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByStatus(ActivityStatus status);
    List<Activity> findByCategory(String category);
    List<Activity> findByCreatedById(Long userId);
}
