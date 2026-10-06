package com.ucc.bienestar360.service;

import com.ucc.bienestar360.dto.CreateActivityRequest;
import com.ucc.bienestar360.model.*;
import com.ucc.bienestar360.repository.ActivityRepository;
import com.ucc.bienestar360.repository.EnrollmentRepository;
import com.ucc.bienestar360.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    public List<Activity> getActivitiesByStatus(ActivityStatus status) {
        return activityRepository.findByStatus(status);
    }

    public Activity getActivityById(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada con ID: " + id));
    }

    @Transactional
    public Activity createActivity(CreateActivityRequest request) {
        User creator = userRepository.findById(request.getCreatedByUserId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario creador no encontrado"));

        if (creator.getRole() != Role.DOCENTE_BIENESTAR && creator.getRole() != Role.ADMIN_INSTITUCIONAL) {
            throw new SecurityException("No tiene permisos para crear actividades de Bienestar. Rol requerido: DOCENTE_BIENESTAR");
        }

        Activity activity = Activity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .location(request.getLocation())
                .startTime(request.getStartTime() != null ? request.getStartTime() : LocalDateTime.now().plusDays(1))
                .endTime(request.getEndTime() != null ? request.getEndTime() : LocalDateTime.now().plusDays(1).plusHours(2))
                .hoursGranted(request.getHoursGranted() != null ? request.getHoursGranted() : 2.0)
                .totalSlots(request.getTotalSlots() != null ? request.getTotalSlots() : 30)
                .availableSlots(request.getTotalSlots() != null ? request.getTotalSlots() : 30)
                .status(ActivityStatus.PROGRAMADA)
                .createdBy(creator)
                .build();

        return activityRepository.save(activity);
    }

    @Transactional
    public Enrollment enrollStudent(Long studentId, Long activityId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));

        if (student.getRole() != Role.ESTUDIANTE) {
            throw new IllegalArgumentException("Solo los estudiantes pueden inscribirse a actividades.");
        }

        Activity activity = getActivityById(activityId);

        if (enrollmentRepository.existsByStudentIdAndActivityId(studentId, activityId)) {
            throw new IllegalStateException("El estudiante ya se encuentra inscrito en esta actividad.");
        }

        if (activity.getAvailableSlots() <= 0) {
            throw new IllegalStateException("No hay cupos disponibles para esta actividad.");
        }

        activity.setAvailableSlots(activity.getAvailableSlots() - 1);
        activityRepository.save(activity);

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .activity(activity)
                .enrollmentDate(LocalDateTime.now())
                .status(EnrollmentStatus.INSCRITO)
                .build();

        return enrollmentRepository.save(enrollment);
    }

    @Transactional
    public Activity updateStatus(Long activityId, ActivityStatus status) {
        Activity activity = getActivityById(activityId);
        activity.setStatus(status);
        return activityRepository.save(activity);
    }
}
