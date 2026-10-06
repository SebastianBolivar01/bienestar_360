package com.ucc.bienestar360.service;

import com.ucc.bienestar360.dto.ScanQrRequest;
import com.ucc.bienestar360.dto.ScanQrResponse;
import com.ucc.bienestar360.model.*;
import com.ucc.bienestar360.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final QrSessionRepository qrSessionRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public ScanQrResponse registerAttendanceViaQr(ScanQrRequest request) {
        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado con ID: " + request.getStudentId()));

        if (student.getRole() != Role.ESTUDIANTE) {
            throw new IllegalArgumentException("Solo los estudiantes pueden registrar su asistencia mediante el escaneo de QR.");
        }

        QrSession session = qrSessionRepository.findByToken(request.getQrToken())
                .orElseThrow(() -> new IllegalArgumentException("Código QR inválido o expirado. Verifique el código mostrado por el docente."));

        if (!session.isActive() || session.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("El código QR se encuentra inactivo o ha expirado.");
        }

        Activity activity = session.getActivity();

        Enrollment enrollment = enrollmentRepository.findByStudentIdAndActivityId(student.getId(), activity.getId())
                .orElseGet(() -> {
                    if (activity.getAvailableSlots() > 0) {
                        activity.setAvailableSlots(activity.getAvailableSlots() - 1);
                        return enrollmentRepository.save(Enrollment.builder()
                                .student(student)
                                .activity(activity)
                                .enrollmentDate(LocalDateTime.now())
                                .status(EnrollmentStatus.INSCRITO)
                                .build());
                    } else {
                        throw new IllegalStateException("No estabas inscrito previamente en la actividad '" + activity.getTitle() + "' y no hay cupos disponibles.");
                    }
                });

        if (attendanceRepository.existsByStudentIdAndActivityId(student.getId(), activity.getId())) {
            throw new IllegalStateException("Ya habías registrado tu asistencia previamente para la actividad '" + activity.getTitle() + "'.");
        }

        double hoursGranted = activity.getHoursGranted();

        Attendance attendance = Attendance.builder()
                .student(student)
                .activity(activity)
                .qrSession(session)
                .timestamp(LocalDateTime.now())
                .status("VERIFICADA")
                .hoursAwarded(hoursGranted)
                .build();
        attendanceRepository.save(attendance);

        double currentHours = student.getCompletedHours() != null ? student.getCompletedHours() : 0.0;
        double newTotal = currentHours + hoursGranted;
        student.setCompletedHours(newTotal);
        userRepository.save(student);

        enrollment.setStatus(EnrollmentStatus.ASISTIO);
        enrollmentRepository.save(enrollment);

        return ScanQrResponse.builder()
                .success(true)
                .message("¡Asistencia registrada con éxito!")
                .activityTitle(activity.getTitle())
                .hoursAwarded(hoursGranted)
                .totalCompletedHours(newTotal)
                .build();
    }

    public List<Attendance> getStudentAttendances(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

    public List<Attendance> getActivityAttendances(Long activityId) {
        return attendanceRepository.findByActivityId(activityId);
    }
}
