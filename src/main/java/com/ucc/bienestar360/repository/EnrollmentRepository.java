package com.ucc.bienestar360.repository;

import com.ucc.bienestar360.model.Enrollment;
import com.ucc.bienestar360.model.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentId(Long studentId);
    List<Enrollment> findByActivityId(Long activityId);
    Optional<Enrollment> findByStudentIdAndActivityId(Long studentId, Long activityId);
    boolean existsByStudentIdAndActivityId(Long studentId, Long activityId);
    long countByActivityIdAndStatus(Long activityId, EnrollmentStatus status);
}
