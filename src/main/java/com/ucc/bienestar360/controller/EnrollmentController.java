package com.ucc.bienestar360.controller;

import com.ucc.bienestar360.model.Enrollment;
import com.ucc.bienestar360.repository.EnrollmentRepository;
import com.ucc.bienestar360.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EnrollmentController {

    private final ActivityService activityService;
    private final EnrollmentRepository enrollmentRepository;

    @PostMapping
    public ResponseEntity<?> enrollStudent(@RequestParam Long studentId, @RequestParam Long activityId) {
        try {
            Enrollment enrollment = activityService.enrollStudent(studentId, activityId);
            return ResponseEntity.ok(enrollment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Enrollment>> getStudentEnrollments(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentRepository.findByStudentId(studentId));
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<List<Enrollment>> getActivityEnrollments(@PathVariable Long activityId) {
        return ResponseEntity.ok(enrollmentRepository.findByActivityId(activityId));
    }
}
