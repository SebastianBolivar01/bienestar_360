package com.ucc.bienestar360.repository;

import com.ucc.bienestar360.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentId(Long studentId);
    List<Attendance> findByActivityId(Long activityId);
    Optional<Attendance> findByStudentIdAndActivityId(Long studentId, Long activityId);
    boolean existsByStudentIdAndActivityId(Long studentId, Long activityId);
}
