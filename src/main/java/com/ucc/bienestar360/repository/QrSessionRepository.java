package com.ucc.bienestar360.repository;

import com.ucc.bienestar360.model.QrSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QrSessionRepository extends JpaRepository<QrSession, Long> {
    Optional<QrSession> findByToken(String token);
    Optional<QrSession> findFirstByActivityIdAndActiveTrueOrderByCreatedAtDesc(Long activityId);
    List<QrSession> findByActivityId(Long activityId);
}
